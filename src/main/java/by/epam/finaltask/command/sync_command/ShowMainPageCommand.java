package by.epam.finaltask.command.sync_command;

import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.SyncCommandResponse;
import by.epam.finaltask.command.UserSessionAttribute;
import by.epam.finaltask.controller.PagePath;
import by.epam.finaltask.model.*;
import by.epam.finaltask.service.CategoryService;
import by.epam.finaltask.service.LotService;
import by.epam.finaltask.service.RegionService;
import by.epam.finaltask.service.ServiceFactory;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ShowMainPageCommand implements SyncCommand {

    private final static Logger LOG = LogManager.getLogger(ShowMainPageCommand.class);

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.NOT_AUTHORIZED, Role.ADMIN, Role.USER));

    private final LotService lotService = ServiceFactory.getFactoryInstance().lotService();
    private final RegionService regionService = ServiceFactory.getFactoryInstance().regionService();
    private final CategoryService categoryService = ServiceFactory.getFactoryInstance().categoryService();

    private final LotContext defaultLotContext = LotContext.builder()
            .setAuctionStatus(AuctionStatus.APPROVED_BY_ADMIN.name()).build();

    ShowMainPageCommand() {
    }

    @Override
    public SyncCommandResponse execute(CommandRequest request) {
        String pageNumberParam = request.getParameter("pageNum");
        try {
            request.setAttribute("regions", regionService.findAllRegions());
            request.setAttribute("categories", categoryService.findAllCategories());
            //todo:validation
            LotContext sessionContext = (LotContext) request.getSession()
                    .getAttribute(UserSessionAttribute.MAIN_PAGE_LOT_CONTEXT.name());
            LotContext context = sessionContext != null ? sessionContext : defaultLotContext;
            request.setAttribute("lotContextJson", new Gson().toJson(context));
            int pageNumber = pageNumberParam == null ? 1 : Integer.parseInt(pageNumberParam);
            request.setAttribute("lots", lotService.findLotsByPageAndContext(pageNumber, context));
        } catch (Exception ex) {
            LOG.warn(ex.getMessage(), ex);
            request.setAttribute("errorMessage", ex.getMessage());
        }
        return new SyncCommandResponse(false, PagePath.MAIN.getPath());
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }
}
