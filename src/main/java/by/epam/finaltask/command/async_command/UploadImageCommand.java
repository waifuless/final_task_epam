package by.epam.finaltask.command.async_command;

import by.epam.finaltask.command.AjaxCommandResponse;
import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.UserSessionAttribute;
import by.epam.finaltask.command.sync_command.SaveLotCommand;
import by.epam.finaltask.model.Role;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class UploadImageCommand implements AjaxCommand {

    private final static Logger LOG = LogManager.getLogger(SaveLotCommand.class);

    private final static String AUCTION_IMAGES_FOLDER = System.getenv("AUCTION_IMAGES_FOLDER");
    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.ADMIN, Role.USER));

    private final String contextImageFolder;

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }

    UploadImageCommand() throws IOException {
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
    public AjaxCommandResponse execute(CommandRequest request) throws Exception {
        try {
            Part image = request.getPart("imageInput");
            if (image != null) {
                String imageFolder = String
                        .format("%s/%s", request.getSession().getAttribute(UserSessionAttribute.USER_ID.name()),
                                LocalDate.now());
                String imageFolderRealPath = String
                        .format("%s/%s", AUCTION_IMAGES_FOLDER, imageFolder);
                Files.createDirectories(Paths.get(imageFolderRealPath));
                String imageName = image.getName() + System.currentTimeMillis();
                String imageRealPath = String.format("%s/%s", imageFolderRealPath, imageName);
                LOG.debug("Image real path: {}", imageRealPath);
                image.write(imageRealPath);
                String imageContextPath = String.format("%s/%s/%s", contextImageFolder, imageFolder, imageName);
                LOG.debug("Image context path: {}", imageContextPath);
                return new AjaxCommandResponse("text", imageContextPath);
            } else {
                LOG.warn("Image=null");
            }
            return null;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw ex;
        }
    }
}
