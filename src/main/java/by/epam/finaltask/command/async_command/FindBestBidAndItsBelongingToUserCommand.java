package by.epam.finaltask.command.async_command;

import by.epam.finaltask.command.AjaxCommandResponse;
import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.UserSessionAttribute;
import by.epam.finaltask.model.Bid;
import by.epam.finaltask.model.Role;
import by.epam.finaltask.service.BidService;
import by.epam.finaltask.service.ServiceFactory;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FindBestBidAndItsBelongingToUserCommand implements AjaxCommand {

    private final static Logger LOG = LoggerFactory.getLogger(FindBestBidAndItsBelongingToUserCommand.class);

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.USER, Role.ADMIN));

    private final BidService bidService = ServiceFactory.getFactoryInstance().bidService();

    FindBestBidAndItsBelongingToUserCommand() {
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }

    @Override
    public AjaxCommandResponse execute(CommandRequest request) throws Exception {
        String lotId = request.getParameter("lotId");
        long userId = (Long) request.getSession().getAttribute(UserSessionAttribute.USER_ID.name());
        Bid bestBid = bidService.findBestBid(userId, lotId);
        Object[] answer = new Object[2];
        answer[0] = bestBid.getAmount();
        answer[1] = userId == bestBid.getUserId();
        LOG.debug("Best bid: {}", bestBid);
        String answerJson = new Gson().toJson(answer);
        return new AjaxCommandResponse("application/json", answerJson);
    }
}
