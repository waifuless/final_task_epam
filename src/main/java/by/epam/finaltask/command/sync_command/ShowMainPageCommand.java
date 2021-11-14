package by.epam.finaltask.command.sync_command;

import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.SyncCommandResponse;
import by.epam.finaltask.controller.PagePath;
import by.epam.finaltask.model.Role;
import by.epam.finaltask.service.LotService;
import by.epam.finaltask.service.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ShowMainPageCommand implements SyncCommand {

    private final static Logger LOG = LogManager.getLogger(ShowMainPageCommand.class);

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.NOT_AUTHORIZED, Role.ADMIN, Role.USER));

    private final LotService lotService = ServiceFactory.getFactoryInstance().lotService();

    ShowMainPageCommand() {
    }

    @Override
    public SyncCommandResponse execute(CommandRequest request) {
        String pageNumberParam = request.getParameter("pageNum");
        try {
            //todo:validation
            int pageNumber = pageNumberParam == null ? 1 : Integer.parseInt(pageNumberParam);
            request.setAttribute("lots", lotService.findLotsByPage(pageNumber));
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
}
