package by.epam.finaltask.command.async_command;

import by.epam.finaltask.command.AjaxCommand;
import by.epam.finaltask.command.AjaxCommandResponse;
import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.model.Role;
import by.epam.finaltask.service.LotService;
import by.epam.finaltask.service.ServiceFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UpdateAuctionStatusCommand implements AjaxCommand {

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.ADMIN));

    private final LotService lotService = ServiceFactory.getFactoryInstance().lotService();

    UpdateAuctionStatusCommand() {
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }

    @Override
    public AjaxCommandResponse execute(CommandRequest request) throws Exception {
        String[] lotIds = request.getParameterValues("ids[]");
        String newStatus = request.getParameter("new_status");
        lotService.updateLotsAuctionStatus(lotIds, newStatus);
        return new AjaxCommandResponse("text", "Status successfully updated");
    }
}
