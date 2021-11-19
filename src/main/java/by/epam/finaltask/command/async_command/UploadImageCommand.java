package by.epam.finaltask.command.async_command;

import by.epam.finaltask.command.AjaxCommandResponse;
import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.UserSessionAttribute;
import by.epam.finaltask.exception.CommandError;
import by.epam.finaltask.exception.CommandExecutionException;
import by.epam.finaltask.model.Role;
import by.epam.finaltask.service.ImagesService;
import by.epam.finaltask.service.ServiceFactory;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UploadImageCommand implements AjaxCommand {

    private final static Logger LOG = LogManager.getLogger(UploadImageCommand.class);

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.ADMIN, Role.USER));

    private final ImagesService imagesService = ServiceFactory.getFactoryInstance().imagesService();

    UploadImageCommand() throws IOException {
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }

    @Override
    public AjaxCommandResponse execute(CommandRequest request) throws Exception {
        Part image = request.getPart("imageInput");
        //todo: move it to service
        if(image.getSize()>5242880){
            throw new CommandExecutionException(CommandError.INVALID_IMAGE);
        }
        long userId = (Long)request.getSession().getAttribute(UserSessionAttribute.USER_ID.name());
        String imageContextPath = imagesService.saveImage(image, userId);
        return new AjaxCommandResponse("text", imageContextPath);
    }
}
