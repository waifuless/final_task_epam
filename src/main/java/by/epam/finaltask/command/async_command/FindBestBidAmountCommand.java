package by.epam.finaltask.command.async_command;

import by.epam.finaltask.command.AjaxCommandResponse;
import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.UserSessionAttribute;
import by.epam.finaltask.model.Role;
import by.epam.finaltask.service.BidService;
import by.epam.finaltask.service.ServiceFactory;
import com.google.gson.Gson;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FindBestBidAmountCommand implements AjaxCommand{

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.USER, Role.ADMIN));

    private final BidService bidService = ServiceFactory.getFactoryInstance().bidService();

    FindBestBidAmountCommand() throws IOException {
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }

    @Override
    public AjaxCommandResponse execute(CommandRequest request) throws Exception {
        String lotId = request.getParameter("lot_id");
        long userId = (Long) request.getSession().getAttribute(UserSessionAttribute.USER_ID.name());
        BigDecimal bestAmount = bidService.findBestBidAmount(userId, lotId);
        String bestAmountJson = new Gson().toJson(bestAmount);
        return new AjaxCommandResponse("application/json", bestAmountJson);
    }
}
