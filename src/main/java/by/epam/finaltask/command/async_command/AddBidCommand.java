package by.epam.finaltask.command.async_command;

import by.epam.finaltask.command.AjaxCommandResponse;
import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.UserSessionAttribute;
import by.epam.finaltask.model.Role;
import by.epam.finaltask.service.BidService;
import by.epam.finaltask.service.ServiceFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AddBidCommand implements AjaxCommand {

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.USER, Role.ADMIN));

    private final BidService bidService = ServiceFactory.getFactoryInstance().bidService();

    AddBidCommand() throws IOException {
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }

    @Override
    public AjaxCommandResponse execute(CommandRequest request) throws Exception {
        String lotId = request.getParameter("lot_id");
        long userId = (Long) request.getSession().getAttribute(UserSessionAttribute.USER_ID.name());
        String bidAmount = request.getParameter("bid_amount");
        bidService.addBid(userId, lotId, bidAmount);
        return new AjaxCommandResponse("text", "Bid successfully added");
    }
}
