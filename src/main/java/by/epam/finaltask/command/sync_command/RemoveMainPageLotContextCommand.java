package by.epam.finaltask.command.sync_command;

import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.SyncCommandResponse;
import by.epam.finaltask.command.UserSessionAttribute;
import by.epam.finaltask.controller.PagePath;
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

public class RemoveMainPageLotContextCommand implements SyncCommand{

    private final static Logger LOG = LogManager.getLogger(RemoveMainPageLotContextCommand.class);

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.NOT_AUTHORIZED, Role.ADMIN, Role.USER));

    RemoveMainPageLotContextCommand() {
    }

    @Override
    public SyncCommandResponse execute(CommandRequest request) {
        try {
            request.getSession().removeAttribute(UserSessionAttribute.MAIN_PAGE_LOT_CONTEXT.name());
            return new SyncCommandResponse(true, request.createCommandPath(SyncCommandVariant.SHOW_MAIN));
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
}
