package by.epam.finaltask.command.async_command;

import by.epam.finaltask.command.AjaxCommandResponse;
import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.UserSessionAttribute;
import by.epam.finaltask.exception.ClientError;
import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.model.Role;
import by.epam.finaltask.service.AuctionParticipationService;
import by.epam.finaltask.service.ServiceFactory;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SaveAuctionParticipationCommand implements AjaxCommand {

    private final static Logger LOG = LoggerFactory.getLogger(SaveAuctionParticipationCommand.class);

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.USER, Role.ADMIN));

    private final AuctionParticipationService auctionParticipationService
            = ServiceFactory.getFactoryInstance().auctionParticipationService();

    SaveAuctionParticipationCommand() {
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }

    @Override
    public AjaxCommandResponse execute(CommandRequest request) throws Exception {
        try {
            long userId = (Long) request.getSession().getAttribute(UserSessionAttribute.USER_ID.name());
            auctionParticipationService.saveParticipant(userId, request.getParameter("lotId"));
            String lotsJson = new Gson().toJson("success!");
            return new AjaxCommandResponse("application/json", lotsJson);
        } catch (NumberFormatException ex) {
            LOG.warn(ex.getMessage(), ex);
            throw new ClientErrorException(ClientError.INVALID_NUMBER);
        }
    }
}
