package by.epam.finaltask.command.sync_command;

import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.SyncCommand;
import by.epam.finaltask.command.SyncCommandResponse;
import by.epam.finaltask.command.UserSessionAttribute;
import by.epam.finaltask.controller.PagePath;
import by.epam.finaltask.exception.ClientError;
import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.Role;
import by.epam.finaltask.model.User;
import by.epam.finaltask.service.ServiceFactory;
import by.epam.finaltask.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SignInCommand implements SyncCommand {

    private final static Logger LOG = LoggerFactory.getLogger(SignInCommand.class);

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.NOT_AUTHORIZED));
    private final static String EMAIL_OR_PASSWORD_INVALID_MSG = "EMAIL_OR_PASSWORD_INVALID";

    private final UserService userService = ServiceFactory.getFactoryInstance().userService();

    SignInCommand() {
    }

    @Override
    public SyncCommandResponse execute(CommandRequest request) throws ClientErrorException,
            ServiceCanNotCompleteCommandRequest {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        try {
            if (isStringEmpty(email) || isStringEmpty(password)) {
                throw new ClientErrorException(ClientError.EMPTY_ARGUMENTS);
            }
            Optional<User> optionalUser = userService.authenticate(email, password);
            if (optionalUser.isPresent()) {
                SignInUser(optionalUser.get(), request);
                return new SyncCommandResponse(true, PagePath.START_PAGE.getPath());
            } else {
                LOG.debug(EMAIL_OR_PASSWORD_INVALID_MSG);
                request.setAttribute(EMAIL_OR_PASSWORD_INVALID_MSG, EMAIL_OR_PASSWORD_INVALID_MSG);
                return new SyncCommandResponse(false, PagePath.SIGN_IN.getPath());
            }
        } catch (Exception ex) {
            LOG.warn(ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }

    private boolean isStringEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private void SignInUser(User user, CommandRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
        }
        session = request.createSession();
        session.setAttribute(UserSessionAttribute.USER_ID.name(), user.getUserId());
        session.setAttribute(UserSessionAttribute.USER_EMAIL.name(), user.getEmail());
        session.setAttribute(UserSessionAttribute.USER_ROLE.name(), user.getRole());
    }
}
