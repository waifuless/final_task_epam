package by.epam.finaltask.command.sync_command;

import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.SyncCommand;
import by.epam.finaltask.command.SyncCommandResponse;
import by.epam.finaltask.command.UserSessionAttribute;
import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.Role;
import by.epam.finaltask.service.LotService;
import by.epam.finaltask.service.ServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CreateLotCommand implements SyncCommand {

    private final static Logger LOG = LoggerFactory.getLogger(CreateLotCommand.class);

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.ADMIN, Role.USER));

    private final LotService lotService = ServiceFactory.getFactoryInstance().lotService();

    @Override
    public SyncCommandResponse execute(CommandRequest request) throws ServiceCanNotCompleteCommandRequest,
            ClientErrorException {
        try {
            long userId = (Long) request.getSession().getAttribute(UserSessionAttribute.USER_ID.name());
            String mainImagePath = request.getParameter("mainImage");
            String[] otherImagePaths = request.getParameterValues("otherImage[]");
            String title = request.getParameter("title");
            String category = request.getParameter("category");
            String auctionType = request.getParameter("auction-type");
            String condition = request.getParameter("condition");
            String description = request.getParameter("description");
            String initPrice = request.getParameter("init-price");
            String auctionStart = request.getParameter("auction-start");
            String duration = request.getParameter("duration");
            String region = request.getParameter("region");
            String cityOrDistrict = request.getParameter("city-or-district");
            lotService.createAndSaveLot(userId, mainImagePath, otherImagePaths, title, category, auctionType,
                    condition, description, initPrice, auctionStart, duration, region, cityOrDistrict);
            return new SyncCommandResponse(true, request
                    .createCommandPath(SyncCommandFactory.SyncCommandVariant.SHOW_MAIN));
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
