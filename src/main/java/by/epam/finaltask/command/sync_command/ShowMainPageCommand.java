package by.epam.finaltask.command.sync_command;

import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.SyncCommandResponse;
import by.epam.finaltask.command.UserSessionAttribute;
import by.epam.finaltask.controller.PagePath;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.AuctionStatus;
import by.epam.finaltask.model.LotContext;
import by.epam.finaltask.model.Role;
import by.epam.finaltask.service.CategoryService;
import by.epam.finaltask.service.LotService;
import by.epam.finaltask.service.RegionService;
import by.epam.finaltask.service.ServiceFactory;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ShowMainPageCommand implements SyncCommand {

    private final static Logger LOG = LogManager.getLogger(ShowMainPageCommand.class);

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.NOT_AUTHORIZED, Role.ADMIN, Role.USER));

    private final RegionService regionService = ServiceFactory.getFactoryInstance().regionService();
    private final CategoryService categoryService = ServiceFactory.getFactoryInstance().categoryService();

    ShowMainPageCommand() {
    }

    @Override
    public SyncCommandResponse execute(CommandRequest request) throws ServiceCanNotCompleteCommandRequest {
        try {
            request.setAttribute("regions", regionService.findAllRegions());
            request.setAttribute("categories", categoryService.findAllCategories());
            request.setAttribute("title", request.getParameter("title"));
            return new SyncCommandResponse(false, PagePath.MAIN.getPath());
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
