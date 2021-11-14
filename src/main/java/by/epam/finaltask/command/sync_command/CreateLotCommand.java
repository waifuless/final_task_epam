package by.epam.finaltask.command.sync_command;

import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.SyncCommandResponse;
import by.epam.finaltask.command.UserSessionAttribute;
import by.epam.finaltask.controller.PagePath;
import by.epam.finaltask.model.Role;
import by.epam.finaltask.service.LotService;
import by.epam.finaltask.service.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CreateLotCommand implements SyncCommand {

    private final static Logger LOG = LogManager.getLogger(CreateLotCommand.class);

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.ADMIN, Role.USER));

    private final LotService lotService = ServiceFactory.getFactoryInstance().lotService();

    @Override
    public SyncCommandResponse execute(CommandRequest request) {
        try {
            long userId = (Long)request.getSession().getAttribute(UserSessionAttribute.USER_ID.name());
            String mainImagePath = request.getParameter("mainImage");
            String[] otherImagePaths = request.getParameterValues("otherImage[]");
            String title = request.getParameter("title");
            String category = request.getParameter("category");
            String auctionType = request.getParameter("auction-type");
            String condition = request.getParameter("condition");
            String description = request.getParameter("description");
            String initPrice = request.getParameter("init-price");
            String auctionStart = request.getParameter("auction-start");
            LOG.debug("auctionStart: {}", auctionStart);
            String duration = request.getParameter("duration");
            String region = request.getParameter("region");
            String cityOrDistrict = request.getParameter("city-or-district");
            if (isStringEmpty(mainImagePath) || isStringEmpty(title) || isStringEmpty(category)
                    || isStringEmpty(auctionType) || isStringEmpty(condition) || isStringEmpty(description)
                    || isStringEmpty(initPrice) || isStringEmpty(auctionStart) || isStringEmpty(duration)
                    || isStringEmpty(region)) {
                LOG.debug("One of required fields is empty");
                request.setAttribute("errorMessage","One of required fields is empty");
                return new SyncCommandResponse(false, PagePath.LOT_CREATION.getPath());
            }
            lotService.createAndSaveLot(userId, mainImagePath, otherImagePaths, title, category, auctionType,
                    condition, description, initPrice, auctionStart, duration, region, cityOrDistrict);
            return new SyncCommandResponse(true, request
                    .createCommandPath(SyncCommandFactory.SyncCommandVariant.SHOW_MAIN));
        } catch (Exception ex) {
            LOG.warn(ex.getMessage(), ex);
            request.setAttribute("errorMessage", ex.getMessage());
            return new SyncCommandResponse(false, PagePath.ERROR.getPath());
        }
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }

    private boolean isStringEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
