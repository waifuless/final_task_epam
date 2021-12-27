package by.epam.finaltask.command.sync_command;

import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.SyncCommand;
import by.epam.finaltask.command.SyncCommandResponse;
import by.epam.finaltask.command.UserSessionAttribute;
import by.epam.finaltask.controller.PagePath;
import by.epam.finaltask.exception.ClientError;
import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.Bid;
import by.epam.finaltask.model.LotWithImages;
import by.epam.finaltask.model.Role;
import by.epam.finaltask.service.AuctionParticipationService;
import by.epam.finaltask.service.BidService;
import by.epam.finaltask.service.LotService;
import by.epam.finaltask.service.ServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ShowLotPageCommand implements SyncCommand {

    private final static Logger LOG = LoggerFactory.getLogger(ShowLotPageCommand.class);

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.NOT_AUTHORIZED, Role.ADMIN, Role.USER));

    private final LotService lotService = ServiceFactory.getFactoryInstance().lotService();

    private final AuctionParticipationService auctionParticipationService
            = ServiceFactory.getFactoryInstance().auctionParticipationService();
    private final BidService bidService = ServiceFactory.getFactoryInstance().bidService();

    ShowLotPageCommand() {
    }

    @Override
    public SyncCommandResponse execute(CommandRequest request) throws ClientErrorException,
            ServiceCanNotCompleteCommandRequest {
        try {
            String lotId = request.getParameter("lot_id");
            Role userRole = (Role) request.getSession().getAttribute(UserSessionAttribute.USER_ROLE.name());
            long userId = userRole.equals(Role.NOT_AUTHORIZED) ?
                    -1 : (Long) request.getSession().getAttribute(UserSessionAttribute.USER_ID.name());
            LotWithImages lot = lotService.findLotWithImagesValidateUserAccess(userId, lotId, userRole);
            request.setAttribute("lot", lot);
            if (!userRole.equals(Role.NOT_AUTHORIZED)) {
                boolean userIsParticipate = auctionParticipationService.isUserParticipateInLotAuction(userId, lotId);
                request.setAttribute("user_is_participate", userIsParticipate);
                if(userIsParticipate){
                    Optional<Bid> optionalBid = bidService.findBestBid(userId, lot.getLotId());
                    boolean userIsWinner = optionalBid.filter(bid -> bid.getUserId()==userId).isPresent();
                    request.setAttribute("user_is_winner", userIsWinner);
                }
            }
            return new SyncCommandResponse(false, PagePath.LOT.getPath());
        } catch (NumberFormatException ex) {
            LOG.warn(ex.getMessage(), ex);
            throw new ClientErrorException(ClientError.INVALID_NUMBER);
        } catch (Exception ex) {
            LOG.warn(ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }
}
