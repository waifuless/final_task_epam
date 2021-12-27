package by.epam.finaltask.service.impl;

import by.epam.finaltask.dao.ImagesDao;
import by.epam.finaltask.exception.ClientError;
import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.service.ImagesService;
import by.epam.finaltask.validation.NumberValidator;
import by.epam.finaltask.validation.ValidatorFactory;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import jakarta.servlet.http.Part;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Properties;

public class ImagesServiceImpl implements ImagesService {

    private final static Logger LOG = LoggerFactory.getLogger(ImagesServiceImpl.class);

    private final static String AUCTION_IMAGES_FOLDER = System.getenv("AUCTION_IMAGES_FOLDER");
    private final static int DEFAULT_IMAGE_SIZE = 900;

    private final NumberValidator numberValidator = ValidatorFactory.getFactoryInstance().idValidator();

    private final ImagesDao imagesDao;
    private final String contextImageFolder;

    ImagesServiceImpl(ImagesDao imagesDao) throws IOException {
        this.imagesDao = imagesDao;
        try (InputStream inputStream = this.getClass().getClassLoader()
                .getResourceAsStream("path-to-images-folder.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            contextImageFolder = properties.getProperty("folder.context.path");
        } catch (IOException ex) {
            LOG.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public String saveImage(Part image, long userId) throws ServiceCanNotCompleteCommandRequest, ClientErrorException {
        try {
            numberValidator.validateNumberIsPositive(userId);
            LOG.debug("Image file name: {}", image.getSubmittedFileName());
            validateImage(image);
            BufferedImage preparedImage = prepareImageToSave(image);

            String oldImageFileName = image.getSubmittedFileName();
            String imageFormat = oldImageFileName.substring(oldImageFileName.lastIndexOf('.') + 1);
            String imageFolder = String.format("%s/%s", userId, LocalDate.now());
            String imageFolderRealPath = String.format("%s/%s", AUCTION_IMAGES_FOLDER, imageFolder);
            Files.createDirectories(Paths.get(imageFolderRealPath));

            String imageName = System.currentTimeMillis() + "." + imageFormat;
            String imageRealPath = String.format("%s/%s", imageFolderRealPath, imageName);
            LOG.debug("Image real path: {}", imageRealPath);
            ImageIO.write(preparedImage, imageFormat, new File(imageRealPath));

            String imageContextPath = String.format("%s/%s/%s", contextImageFolder, imageFolder, imageName);
            LOG.debug("Image context path: {}", imageContextPath);
            imagesDao.saveImagePath(imageContextPath);
            return imageContextPath;
        } catch (ClientErrorException ex) {
            LOG.warn(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }

    private void validateImage(Part image) throws ClientErrorException {
        if (image.getSize() > 5242880) {
            throw new ClientErrorException(ClientError.INVALID_IMAGE);
        }
        //todo: other validation
    }

    private BufferedImage prepareImageToSave(Part image) throws IOException, ImageProcessingException,
            MetadataException {
        BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
        BufferedImage resizedBufferedImage = Scalr.resize(bufferedImage, DEFAULT_IMAGE_SIZE);
        Optional<Scalr.Rotation> rotation = findRotation(image);
        if (rotation.isPresent()) {
            resizedBufferedImage = Scalr.rotate(resizedBufferedImage, rotation.get());
        }
        return resizedBufferedImage;
    }

    private Optional<Scalr.Rotation> findRotation(Part image) throws IOException, ImageProcessingException,
            MetadataException {
        Metadata metadata = ImageMetadataReader.readMetadata(image.getInputStream());
        ExifIFD0Directory exifIFD0 = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        if (exifIFD0 == null || !exifIFD0.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
            return Optional.empty();
        }
        int orientation = exifIFD0.getInt(ExifIFD0Directory.TAG_ORIENTATION);

        switch (orientation) {
            case 6: // [Exif IFD0] Orientation - Right side, top (Rotate 90 CW)
                return Optional.of(Scalr.Rotation.CW_90);
            case 3: // [Exif IFD0] Orientation - Bottom, right side (Rotate 180)
                return Optional.of(Scalr.Rotation.CW_180);
            case 8: // [Exif IFD0] Orientation - Left side, bottom (Rotate 270 CW)
                return Optional.of(Scalr.Rotation.CW_270);
            default: // [Exif IFD0] Orientation - Top, left side (Horizontal / normal)
                return Optional.empty();
        }
    }
}
