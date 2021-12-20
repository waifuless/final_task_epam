package by.epam.finaltask.command.async_command;

import by.epam.finaltask.command.AjaxCommandResponse;
import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.exception.ClientError;
import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.model.Role;
import by.epam.finaltask.service.LotService;
import by.epam.finaltask.service.ServiceFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ValidateDateInLotCreationCommand implements AjaxCommand {

    private final static String VALIDATION_PASSED_MSG = "date is valid";

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.ADMIN, Role.USER));

    private final LotService lotService = ServiceFactory.getFactoryInstance().lotService();

    ValidateDateInLotCreationCommand() {
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }

    @Override
    public AjaxCommandResponse execute(CommandRequest request) throws Exception {
        String auctionStart = request.getParameter("auction-start");
        if (auctionStart == null || auctionStart.trim().isEmpty()) {
            throw new ClientErrorException(ClientError.EMPTY_ARGUMENTS);
        }
        try {
            lotService.validateAuctionStartDate(auctionStart);
            return new AjaxCommandResponse("text", VALIDATION_PASSED_MSG);
        } catch (ClientErrorException ex) {
            throw new ClientErrorException(ClientError.INVALID_ARGUMENTS);
        }
    }
}
