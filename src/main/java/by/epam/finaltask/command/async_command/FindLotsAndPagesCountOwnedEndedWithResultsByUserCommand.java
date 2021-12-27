package by.epam.finaltask.command.async_command;

import by.epam.finaltask.command.AjaxCommand;
import by.epam.finaltask.command.AjaxCommandResponse;
import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.UserSessionAttribute;
import by.epam.finaltask.exception.ClientError;
import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.model.*;
import by.epam.finaltask.service.BidService;
import by.epam.finaltask.service.LotService;
import by.epam.finaltask.service.ServiceFactory;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FindLotsAndPagesCountOwnedEndedWithResultsByUserCommand implements AjaxCommand {

    private final static Logger LOG = LoggerFactory
            .getLogger(FindLotsAndPagesCountOwnedEndedWithResultsByUserCommand.class);

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.USER, Role.ADMIN));

    private final BidService bidService = ServiceFactory.getFactoryInstance().bidService();
    private final LotService lotService = ServiceFactory.getFactoryInstance().lotService();

    FindLotsAndPagesCountOwnedEndedWithResultsByUserCommand() {
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
            Map<LotWithImages, AuctionResult> resultsByLots = bidService
                    .findUserOwnedEndedLotsWithAuctionResult(pageNumber, userId);
            long pagesCount = lotService.findLotPagesCount(LotContext.builder()
                    .setOwnerId(userId)
                    .setAuctionStatus(AuctionStatus.ENDED.name())
                    .build());

            Gson gson = new Gson().newBuilder().enableComplexMapKeySerialization().serializeNulls().create();
            Object[] answer = new Object[2];
            answer[0] = resultsByLots;
            answer[1] = pagesCount;
            String lotsJson = gson.toJson(answer);
            LOG.debug("Answer: {}", lotsJson);
            return new AjaxCommandResponse("application/json", lotsJson);
        } catch (NumberFormatException ex) {
            LOG.warn(ex.getMessage(), ex);
            throw new ClientErrorException(ClientError.INVALID_NUMBER);
        }
    }
}
