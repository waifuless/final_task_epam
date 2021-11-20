package by.epam.finaltask.command.async_command;

import by.epam.finaltask.command.AjaxCommandResponse;
import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.SyncCommandResponse;
import by.epam.finaltask.command.UserSessionAttribute;
import by.epam.finaltask.command.async_command.AjaxCommand;
import by.epam.finaltask.command.sync_command.SyncCommandFactory;
import by.epam.finaltask.exception.ClientError;
import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.model.*;
import by.epam.finaltask.service.CityOrDistrictService;
import by.epam.finaltask.service.LotService;
import by.epam.finaltask.service.ServiceFactory;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FindLotsByAdminCommand implements AjaxCommand {

    private final static Logger LOG = LogManager.getLogger(FindLotsByAdminCommand.class);

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.ADMIN));

    private final LotService lotService = ServiceFactory.getFactoryInstance().lotService();

    FindLotsByAdminCommand() {
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }

    @Override
    public AjaxCommandResponse execute(CommandRequest request) throws Exception {
        try {
            String pageNumberParam = request.getParameter("pageNum");
            int pageNumber = pageNumberParam == null ? 1 : Integer.parseInt(pageNumberParam);

            LotContext context = findLotContext(request);
            LOG.debug("Lot by admin context: {}", context);
            List<LotWithImages> lots = lotService.findLotsByPageAndContext(pageNumber, context);
            String lotsJson = new Gson().toJson(lots);
            return new AjaxCommandResponse("application/json", lotsJson);
        }
        catch (NumberFormatException ex){
            LOG.warn(ex.getMessage(), ex);
            throw new ClientErrorException(ClientError.INVALID_NUMBER);
        }
    }

    private LotContext findLotContext(CommandRequest request) throws NumberFormatException{
        String ownerIdParam = request.getParameter("owner-id");
        Long ownerId = ownerIdParam==null || ownerIdParam.trim().isEmpty() ? null : Long.parseLong(ownerIdParam);
        return LotContext.builder()
                .setOwnerId(ownerId)
                .setTitle(retrieveNullIfEmpty(request.getParameter("title")))
                .setCategory(retrieveNullIfEmpty(request.getParameter("category")))
                .setAuctionType(retrieveNullIfEmpty(request.getParameter("auction-type")))
                .setMinInitialPrice(parseAndRetrieveNullIfEmpty(request.getParameter("price-from")))
                .setMaxInitialPrice(parseAndRetrieveNullIfEmpty(request.getParameter("price-to")))
                .setRegion(retrieveNullIfEmpty(request.getParameter("region")))
                .setCityOrDistrict(retrieveNullIfEmpty(request.getParameter("city-or-district")))
                .setAuctionStatus(retrieveNullIfEmpty(request.getParameter("auction-status")))
                .setProductCondition(retrieveNullIfEmpty(request.getParameter("product-condition")))
                .build();
    }

    private String retrieveNullIfEmpty(String param) {
        return param == null || param.trim().isEmpty() ? null : param;
    }

    private BigDecimal parseAndRetrieveNullIfEmpty(String param){
        return (param == null || param.trim().isEmpty()) ? null : new BigDecimal(param);
    }
}
