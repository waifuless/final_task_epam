package by.epam.finaltask.command.sync_command;

import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.SyncCommandResponse;
import by.epam.finaltask.controller.PagePath;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ShowAdminNewLotsCommand implements SyncCommand {

    private final static Logger LOG = LogManager.getLogger(ShowAdminNewLotsCommand.class);

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.ADMIN));

    @Override
    public SyncCommandResponse execute(CommandRequest request) throws ServiceCanNotCompleteCommandRequest {
        return new SyncCommandResponse(false, PagePath.ADMIN_NEW_LOTS.getPath());
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }
}
