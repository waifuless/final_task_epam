package by.epam.finaltask.command.sync_command;

import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.SyncCommand;
import by.epam.finaltask.command.SyncCommandResponse;
import by.epam.finaltask.controller.PagePath;
import by.epam.finaltask.model.Role;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ShowSingInPageCommand implements SyncCommand {

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.NOT_AUTHORIZED));

    ShowSingInPageCommand() {
    }

    @Override
    public SyncCommandResponse execute(CommandRequest request) {
        return new SyncCommandResponse(false, PagePath.SIGN_IN.getPath());
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }
}
