package by.epam.finaltask.service;

import by.epam.finaltask.dao.ImagesManager;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Properties;

public class CommonImagesService implements ImagesService {

    private final static Logger LOG = LogManager.getLogger(CommonImagesService.class);

    private final static String AUCTION_IMAGES_FOLDER = System.getenv("AUCTION_IMAGES_FOLDER");

    private final ImagesManager imagesManager;
    private final String contextImageFolder;

    CommonImagesService(ImagesManager imagesManager) throws IOException {
        this.imagesManager = imagesManager;
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
    public String saveImage(Part image, long userId) throws ServiceCanNotCompleteCommandRequest {
        try {
            String imageFolder = String.format("%s/%s", userId, LocalDate.now());
            String imageFolderRealPath = String.format("%s/%s", AUCTION_IMAGES_FOLDER, imageFolder);
            Files.createDirectories(Paths.get(imageFolderRealPath));

            String imageName = String.valueOf(System.currentTimeMillis());
            String imageRealPath = String.format("%s/%s", imageFolderRealPath, imageName);
            LOG.debug("Image real path: {}", imageRealPath);
            image.write(imageRealPath);

            String imageContextPath = String.format("%s/%s/%s", contextImageFolder, imageFolder, imageName);
            LOG.debug("Image context path: {}", imageContextPath);
            imagesManager.saveImagePath(imageContextPath);
            return imageContextPath;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new ServiceCanNotCompleteCommandRequest(ex);
        }
    }
}
