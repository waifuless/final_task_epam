package by.epam.finaltask.command;

import by.epam.finaltask.controller.CommandRequest;
import by.epam.finaltask.controller.CommandResponse;
import by.epam.finaltask.controller.PagePath;
import by.epam.finaltask.controller.UserSessionAttribute;
import by.epam.finaltask.model.Role;
import by.epam.finaltask.model.User;
import by.epam.finaltask.service.CommonUserService;
import by.epam.finaltask.service.ServiceFactory;
import by.epam.finaltask.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SignInCommand implements Command {

    private final static Logger LOG = LogManager.getLogger(CommonUserService.class);

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.NOT_AUTHORIZED));
    private final static String ERROR_ATTRIBUTE_NAME = "sign_in_error";
    private final static String CAN_NOT_SIGN_IN_MCG = "email_or_password_incorrect";

    private final UserService userService = ServiceFactory.getFactoryInstance().userService();

    SignInCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        try {
            Optional<User> optionalUser = userService.authenticate(email, password);
            if (optionalUser.isPresent()) {
                SignInUser(optionalUser.get(), request);
                return new CommandResponse(true, PagePath.START_PAGE.getPath());
            } else {
                request.setAttribute(ERROR_ATTRIBUTE_NAME, CAN_NOT_SIGN_IN_MCG);
                return new CommandResponse(false, PagePath.SIGN_IN.getPath());
            }
        } catch (Exception ex) {
            LOG.warn(ex.getMessage(), ex);
            request.setAttribute("errorMessage", ex.getMessage());
            return new CommandResponse(false, PagePath.ERROR.getPath());
        }
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
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
