package by.epam.finaltask.command.sync_command;

import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.SyncCommand;
import by.epam.finaltask.command.SyncCommandResponse;
import by.epam.finaltask.controller.PagePath;
import by.epam.finaltask.model.Role;
import jakarta.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SignOutCommand implements SyncCommand {

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.ADMIN, Role.USER));

    SignOutCommand() {
    }

    @Override
    public SyncCommandResponse execute(CommandRequest request) {
        destroySession(request);
        return new SyncCommandResponse(true, PagePath.START_PAGE.getPath());
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }

    private void destroySession(CommandRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
        }
    }
}
