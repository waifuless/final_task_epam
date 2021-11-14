package by.epam.finaltask.command.sync_command;

import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.SyncCommandResponse;
import by.epam.finaltask.controller.PagePath;
import by.epam.finaltask.model.Category;
import by.epam.finaltask.model.LotWithImages;
import by.epam.finaltask.model.Region;
import by.epam.finaltask.model.Role;
import by.epam.finaltask.service.CategoryService;
import by.epam.finaltask.service.CityOrDistrictService;
import by.epam.finaltask.service.RegionService;
import by.epam.finaltask.service.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ShowLotCreationCommand implements SyncCommand{

    private final static Logger LOG = LogManager.getLogger(ShowLotCreationCommand.class);

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.ADMIN, Role.USER));

    private final RegionService regionService = ServiceFactory.getFactoryInstance().regionService();
    private final CategoryService categoryService = ServiceFactory.getFactoryInstance().categoryService();

    @Override
    public SyncCommandResponse execute(CommandRequest request) {
        try {
            request.setAttribute("regions", regionService.findAllRegions());
            request.setAttribute("categories", categoryService.findAllCategories());
            return new SyncCommandResponse(false, PagePath.LOT_CREATION.getPath());
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
