package by.epam.finaltask.command.async_command;

import by.epam.finaltask.command.AjaxCommand;
import by.epam.finaltask.command.AjaxCommandResponse;
import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.exception.ClientError;
import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.model.Role;
import by.epam.finaltask.model.User;
import by.epam.finaltask.model.UserContext;
import by.epam.finaltask.service.ServiceFactory;
import by.epam.finaltask.service.UserService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FindUsersAndPagesCountByAdminCommand implements AjaxCommand {

    private final static Logger LOG = LoggerFactory.getLogger(FindUsersAndPagesCountByAdminCommand.class);

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.ADMIN));

    private final UserService userService = ServiceFactory.getFactoryInstance().userService();

    FindUsersAndPagesCountByAdminCommand() {
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }

    @Override
    public AjaxCommandResponse execute(CommandRequest request) throws Exception {
        try {
            String pageNumberParam = request.getParameter("page");
            long pageNumber = pageNumberParam == null ? 1 : Integer.parseInt(pageNumberParam);
            LOG.debug("PageParam:{}, PageToFind: {}", pageNumberParam, pageNumber);
            UserContext context = findUserContext(request);
            LOG.debug("Lot by user context: {}", context);
            List<User> users = userService.findUsersByPageAndContext(pageNumber, context);
            long pagesCount = userService.findUsersPagesCount(context);
            Object[] answer = new Object[2];
            answer[0] = users;
            answer[1] = pagesCount;
            String usersJson = new Gson().toJson(answer);
            return new AjaxCommandResponse("application/json", usersJson);
        } catch (NumberFormatException ex) {
            LOG.warn(ex.getMessage(), ex);
            throw new ClientErrorException(ClientError.INVALID_NUMBER);
        }
    }

    private UserContext findUserContext(CommandRequest request) throws NumberFormatException {
        return UserContext.builder()
                .setUserId(retrieveNullLongIfEmpty(request.getParameter("user-id")))
                .setEmail(retrieveNullStringIfEmpty(request.getParameter("email")))
                .setRole(Role.USER)
                .setBanned(retrieveNullBooleanIfEmpty(request.getParameter("ban-status")))
                .build();
    }

    private String retrieveNullStringIfEmpty(String param) {
        return param == null || param.trim().isEmpty() ? null : param;
    }

    private Boolean retrieveNullBooleanIfEmpty(String param) {
        return param == null || param.trim().isEmpty() ? null : Boolean.valueOf(param);
    }

    private Long retrieveNullLongIfEmpty(String param) {
        return param == null || param.trim().isEmpty() ? null : Long.valueOf(param);
    }
}
