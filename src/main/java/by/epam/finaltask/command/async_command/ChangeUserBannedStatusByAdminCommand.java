package by.epam.finaltask.command.async_command;

import by.epam.finaltask.command.AjaxCommandResponse;
import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.exception.ClientError;
import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.model.Role;
import by.epam.finaltask.service.ServiceFactory;
import by.epam.finaltask.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChangeUserBannedStatusByAdminCommand implements AjaxCommand {

    private final static Logger LOG = LoggerFactory.getLogger(ChangeUserBannedStatusByAdminCommand.class);

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.ADMIN));

    private final UserService userService = ServiceFactory.getFactoryInstance().userService();

    ChangeUserBannedStatusByAdminCommand() {
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }

    @Override
    public AjaxCommandResponse execute(CommandRequest request) throws Exception {
        try {
            String idParam = request.getParameter("id");
            String action = request.getParameter("action");
            if (idParam == null || action == null) {
                throw new ClientErrorException(ClientError.EMPTY_ARGUMENTS);
            }
            userService.changeUserBannedStatus(Long.parseLong(idParam), action);
            return new AjaxCommandResponse("text", "Categories successfully removed");
        } catch (NumberFormatException ex) {
            LOG.warn(ex.getMessage(), ex);
            throw new ClientErrorException(ClientError.INVALID_NUMBER);
        }
    }
}
