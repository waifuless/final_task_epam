package by.epam.finaltask.command;

import by.epam.finaltask.controller.CommandRequest;
import by.epam.finaltask.controller.CommandResponse;
import by.epam.finaltask.controller.PagePath;
import by.epam.finaltask.model.LotWithImages;
import by.epam.finaltask.model.Role;
import by.epam.finaltask.service.LotService;
import by.epam.finaltask.service.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ShowMainPageCommand implements Command {

    private final static Logger LOG = LogManager.getLogger(ShowLotPageCommand.class);

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.NOT_AUTHORIZED, Role.ADMIN, Role.USER));

    private final LotService lotService = ServiceFactory.getFactoryInstance().lotService();

    ShowMainPageCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        String pageNumberParam = request.getParameter("pageNum");
        try {
            //todo:validation
            int pageNumber = pageNumberParam==null ? 1 : Integer.parseInt(pageNumberParam);
            request.setAttribute("lots", lotService.findLotsByPage(pageNumber));
        }catch (Exception ex) {
            LOG.warn(ex.getMessage(), ex);
            request.setAttribute("errorMessage", ex.getMessage());
            return new CommandResponse(false, PagePath.ERROR.getPath());
        }
        return new CommandResponse(false, PagePath.MAIN.getPath());
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }
}
