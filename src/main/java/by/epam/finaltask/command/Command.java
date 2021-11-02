package by.epam.finaltask.command;

import by.epam.finaltask.controller.CommandRequest;
import by.epam.finaltask.controller.CommandResponse;
import by.epam.finaltask.model.Role;

import java.util.List;

public interface Command {

    CommandResponse execute(CommandRequest request);

    List<Role> getAllowedRoles();
}
