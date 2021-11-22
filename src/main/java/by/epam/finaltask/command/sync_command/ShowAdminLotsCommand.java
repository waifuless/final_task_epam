package by.epam.finaltask.command.sync_command;

import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.SyncCommandResponse;
import by.epam.finaltask.controller.PagePath;
import by.epam.finaltask.model.Role;
import by.epam.finaltask.service.CategoryService;
import by.epam.finaltask.service.RegionService;
import by.epam.finaltask.service.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ShowAdminLotsCommand implements SyncCommand{

    private final static Logger LOG = LogManager.getLogger(ShowAdminLotsCommand.class);

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.ADMIN));

    private final RegionService regionService = ServiceFactory.getFactoryInstance().regionService();
    private final CategoryService categoryService = ServiceFactory.getFactoryInstance().categoryService();

    ShowAdminLotsCommand() {
    }

    @Override
    public SyncCommandResponse execute(CommandRequest request) {
        try {
        request.setAttribute("regions", regionService.findAllRegions());
        request.setAttribute("categories", categoryService.findAllCategories());
        } catch (Exception ex) {
            LOG.warn(ex.getMessage(), ex);
            request.setAttribute("errorMessage", ex.getMessage());
        }
        return new SyncCommandResponse(false, PagePath.ADMIN_ALL_LOTS.getPath());
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }
}
