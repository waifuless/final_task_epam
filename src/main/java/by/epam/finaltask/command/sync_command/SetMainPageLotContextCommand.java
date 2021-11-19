package by.epam.finaltask.command.sync_command;

import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.SyncCommandResponse;
import by.epam.finaltask.command.UserSessionAttribute;
import by.epam.finaltask.exception.ClientError;
import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.model.AuctionStatus;
import by.epam.finaltask.model.LotContext;
import by.epam.finaltask.model.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static by.epam.finaltask.command.sync_command.SyncCommandFactory.SyncCommandVariant;

public class SetMainPageLotContextCommand implements SyncCommand {

    private final static Logger LOG = LogManager.getLogger(SetMainPageLotContextCommand.class);

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.NOT_AUTHORIZED, Role.ADMIN, Role.USER));

    SetMainPageLotContextCommand() {
    }

    @Override
    public SyncCommandResponse execute(CommandRequest request) throws ClientErrorException {
        try {
            LotContext context = findLotContext(request);
            request.getSession().setAttribute(UserSessionAttribute.MAIN_PAGE_LOT_CONTEXT.name(), context);
            return new SyncCommandResponse(true, request.createCommandPath(SyncCommandVariant.SHOW_MAIN));
        }
        catch (NumberFormatException ex){
            LOG.warn(ex.getMessage(), ex);
            throw new ClientErrorException(ClientError.INVALID_NUMBER);
        }
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }

    private LotContext findLotContext(CommandRequest request) throws NumberFormatException{
        return LotContext.builder()
                .setCategory(retrieveNullIfEmpty(request.getParameter("category")))
                .setAuctionType(retrieveNullIfEmpty(request.getParameter("auction-type")))
                .setMinInitialPrice(parseAndRetrieveNullIfEmpty(request.getParameter("price-from")))
                .setMaxInitialPrice(parseAndRetrieveNullIfEmpty(request.getParameter("price-to")))
                .setRegion(retrieveNullIfEmpty(request.getParameter("region")))
                .setCityOrDistrict(retrieveNullIfEmpty(request.getParameter("city-or-district")))
                .setAuctionStatus(AuctionStatus.APPROVED_BY_ADMIN.name())
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
