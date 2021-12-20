package by.epam.finaltask.command.sync_command;

import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.SyncCommandResponse;
import by.epam.finaltask.controller.PagePath;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.Role;
import by.epam.finaltask.service.CategoryService;
import by.epam.finaltask.service.RegionService;
import by.epam.finaltask.service.ServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ShowAdminLotsCommand implements SyncCommand {

    private final static Logger LOG = LoggerFactory.getLogger(ShowAdminLotsCommand.class);

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.ADMIN));

    private final RegionService regionService = ServiceFactory.getFactoryInstance().regionService();
    private final CategoryService categoryService = ServiceFactory.getFactoryInstance().categoryService();

    ShowAdminLotsCommand() {
    }

    @Override
    public SyncCommandResponse execute(CommandRequest request) throws ServiceCanNotCompleteCommandRequest {
        try {
            request.setAttribute("regions", regionService.findAllRegions());
            request.setAttribute("categories", categoryService.findAllCategories());
            return new SyncCommandResponse(false, PagePath.ADMIN_ALL_LOTS.getPath());
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
