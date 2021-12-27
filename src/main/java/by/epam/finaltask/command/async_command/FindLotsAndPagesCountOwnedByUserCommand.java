package by.epam.finaltask.command.async_command;

import by.epam.finaltask.command.AjaxCommand;
import by.epam.finaltask.command.AjaxCommandResponse;
import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.UserSessionAttribute;
import by.epam.finaltask.exception.ClientError;
import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.model.LotContext;
import by.epam.finaltask.model.LotWithImages;
import by.epam.finaltask.model.Role;
import by.epam.finaltask.service.LotService;
import by.epam.finaltask.service.ServiceFactory;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FindLotsAndPagesCountOwnedByUserCommand implements AjaxCommand {

    private final static Logger LOG = LoggerFactory.getLogger(FindLotsAndPagesCountOwnedByUserCommand.class);

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.USER, Role.ADMIN));

    private final LotService lotService = ServiceFactory.getFactoryInstance().lotService();

    FindLotsAndPagesCountOwnedByUserCommand() {
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
            long userId = (Long) request.getSession().getAttribute(UserSessionAttribute.USER_ID.name());
            LotContext context = LotContext.builder().setOwnerId(userId).build();
            LOG.debug("Lot by user context: {}", context);
            List<LotWithImages> lots = lotService.findLotsByPageAndContext(pageNumber, context);
            long pagesCount = lotService.findLotPagesCount(context);
            Object[] answer = new Object[2];
            answer[0] = lots;
            answer[1] = pagesCount;
            String lotsJson = new Gson().toJson(answer);
            return new AjaxCommandResponse("application/json", lotsJson);
        } catch (NumberFormatException ex) {
            LOG.warn(ex.getMessage(), ex);
            throw new ClientErrorException(ClientError.INVALID_NUMBER);
        }
    }
}
