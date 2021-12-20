package by.epam.finaltask.command.sync_command;

import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.SyncCommandResponse;
import by.epam.finaltask.controller.PagePath;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.Role;
import by.epam.finaltask.service.RegisterError;
import by.epam.finaltask.service.ServiceFactory;
import by.epam.finaltask.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RegisterCommand implements SyncCommand {

    private final static Logger LOG = LoggerFactory.getLogger(RegisterCommand.class);

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays.asList(Role.NOT_AUTHORIZED));

    private final UserService userService = ServiceFactory.getFactoryInstance().userService();

    RegisterCommand() {
    }

    @Override
    public SyncCommandResponse execute(CommandRequest request) throws ServiceCanNotCompleteCommandRequest {
        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String passwordRepeat = request.getParameter("passwordRepeat");
            List<RegisterError> errors = userService.register(email, password, passwordRepeat);
            if (!errors.isEmpty()) {
                if (email != null) {
                    request.setAttribute("email", email);
                }
                setErrorAttributes(errors, request);
                return new SyncCommandResponse(false, PagePath.REGISTRATION.getPath());
            }
            return new SyncCommandResponse(true, request
                    .createCommandPath(SyncCommandFactory.SyncCommandVariant.SHOW_SIGN_IN));
        } catch (Exception ex) {
            LOG.warn(ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }

    private void setErrorAttributes(List<RegisterError> errors, CommandRequest request) {
        for (RegisterError error : errors) {
            request.setAttribute(error.name(), error);
        }
    }


}
