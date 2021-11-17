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

    ShowMainPageCommand() {
    }

    @Override
    public SyncCommandResponse execute(CommandRequest request) {
        String pageNumberParam = request.getParameter("pageNum");
//        String withFiltersParam = request.getParameter("withFilters");
//        boolean withFilters = Boolean.parseBoolean(withFiltersParam);
        try {
            request.setAttribute("regions", regionService.findAllRegions());
            request.setAttribute("categories", categoryService.findAllCategories());
            //todo:validation
            int pageNumber = pageNumberParam == null ? 1 : Integer.parseInt(pageNumberParam);
            request.setAttribute("lots", lotService.findLotsByPageAndContext(pageNumber,
                    findLotContext(request)));
        } catch (Exception ex) {
            LOG.warn(ex.getMessage(), ex);
            request.setAttribute("errorMessage", ex.getMessage());
            return new SyncCommandResponse(false, PagePath.ERROR.getPath());
        }
        return new SyncCommandResponse(false, PagePath.MAIN.getPath());
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }

    private LotContext findLotContext(CommandRequest request) {
        HttpSession session = request.getSession();
        String minInitPriceParam = request.getParameter("price-from");
        String maxInitPriceParam = request.getParameter("price-to");
        return LotContext.builder().setOwnerId(session == null ?
                        null : (Long) session.getAttribute(UserSessionAttribute.USER_ID.name()))
                .setCategory(retrieveNullIfEmpty(request.getParameter("category")))
                .setAuctionType(retrieveNullIfEmpty(request.getParameter("auction-type")))
                .setTitle(retrieveNullIfEmpty(request.getParameter("title")))
                .setMinInitialPrice((minInitPriceParam == null || minInitPriceParam.isEmpty()) ?
                        null : new BigDecimal(minInitPriceParam))
                .setMaxInitialPrice((maxInitPriceParam == null  || maxInitPriceParam.isEmpty())?
                        null : new BigDecimal(maxInitPriceParam))
                .setRegion(retrieveNullIfEmpty(request.getParameter("region")))
                .setCityOrDistrict(retrieveNullIfEmpty(request.getParameter("city-or-district")))
                .setAuctionStatus(AuctionStatus.APPROVED_BY_ADMIN.name())
                .setProductCondition(retrieveNullIfEmpty(request.getParameter("product-condition")))
                .build();
    }

    private String retrieveNullIfEmpty(String param){
        return param==null || param.isEmpty() ? null : param;
    }
}
