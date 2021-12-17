package by.epam.finaltask.command.async_command;

import by.epam.finaltask.command.AjaxCommandResponse;
import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.UserSessionAttribute;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.Role;
import by.epam.finaltask.model.User;
import by.epam.finaltask.service.ServiceFactory;
import by.epam.finaltask.service.UserService;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FindUserCashAccountCommand implements AjaxCommand {

    private final static Logger LOG = LogManager.getLogger(FindUserCashAccountCommand.class);
    private final static String USER_NOT_FOUND_MCG = "user not found";

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.USER, Role.ADMIN));

    private final UserService userService = ServiceFactory.getFactoryInstance().userService();

    FindUserCashAccountCommand() {
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }

    @Override
    public AjaxCommandResponse execute(CommandRequest request) throws Exception {
        try {
            long userId = (Long) request.getSession().getAttribute(UserSessionAttribute.USER_ID.name());
            Optional<User> optionalUser = userService.findUserById(userId);
            User user = optionalUser.orElseThrow(() -> new ServiceCanNotCompleteCommandRequest(USER_NOT_FOUND_MCG));
            String lotsJson = new Gson().toJson(user.getCashAccount());
            return new AjaxCommandResponse("application/json", lotsJson);
        } catch (Exception ex) {
            LOG.warn(ex.getMessage());
            throw ex;
        }
    }
}
