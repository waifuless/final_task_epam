package by.epam.finaltask.command.sync_command;

import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.SyncCommandResponse;
import by.epam.finaltask.controller.PagePath;
import by.epam.finaltask.exception.CommandError;
import by.epam.finaltask.exception.CommandExecutionException;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
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

public class ShowLotPageCommand implements SyncCommand {

    private final static Logger LOG = LogManager.getLogger(ShowLotPageCommand.class);

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.NOT_AUTHORIZED, Role.ADMIN, Role.USER));

    private final LotService lotService = ServiceFactory.getFactoryInstance().lotService();

    ShowLotPageCommand() {
    }

    @Override
    public SyncCommandResponse execute(CommandRequest request) throws CommandExecutionException,
            ServiceCanNotCompleteCommandRequest {
        try {
            int lot_id = Integer.parseInt(request.getParameter("lot_id"));
            Optional<LotWithImages> optionalLotWithImages = lotService.findLot(lot_id);
            if (optionalLotWithImages.isPresent()) {
                request.setAttribute("lot", optionalLotWithImages.get());
            } else {
                throw new CommandExecutionException(CommandError.NOT_FOUND);
            }
            return new SyncCommandResponse(false, PagePath.LOT.getPath());
        }catch (NumberFormatException ex){
            LOG.warn(ex.getMessage(), ex);
            throw new CommandExecutionException(CommandError.INVALID_NUMBER);
        }
        catch (Exception ex) {
            LOG.warn(ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }
}
