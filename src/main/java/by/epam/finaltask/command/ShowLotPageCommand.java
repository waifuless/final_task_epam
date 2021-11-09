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
import java.util.Optional;

public class ShowLotPageCommand implements Command {

    private final static Logger LOG = LogManager.getLogger(ShowLotPageCommand.class);

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.NOT_AUTHORIZED, Role.ADMIN, Role.USER));

    private final LotService lotService = ServiceFactory.getFactoryInstance().lotService();

    ShowLotPageCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        try {
            //todo: validate parsing string to int
            int lot_id = Integer.parseInt(request.getParameter("lot_id"));
            Optional<LotWithImages> optionalLotWithImages = lotService.findLot(lot_id);
            if (optionalLotWithImages.isPresent()){
                request.setAttribute("lot", optionalLotWithImages.get());
            }else{
                //todo: remake
                throw new RuntimeException();
            }
            return new CommandResponse(false, PagePath.LOT.getPath());
        } catch (Exception ex) {
            LOG.warn(ex.getMessage(), ex);
            request.setAttribute("errorMessage", ex.getMessage());
            return new CommandResponse(false, PagePath.ERROR.getPath());
        }
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }
}
