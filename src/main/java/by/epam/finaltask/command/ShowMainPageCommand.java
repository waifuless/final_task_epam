package by.epam.finaltask.command;

import by.epam.finaltask.controller.CommandRequest;
import by.epam.finaltask.controller.CommandResponse;
import by.epam.finaltask.controller.PagePath;
import by.epam.finaltask.model.Role;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ShowMainPageCommand implements Command {

    private final static List<Role> ALLOWED_ROLES = Collections.unmodifiableList(Arrays
            .asList(Role.NOT_AUTHORIZED, Role.ADMIN, Role.USER));


    ShowMainPageCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return new CommandResponse(false, PagePath.MAIN.getPath());
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }
}
