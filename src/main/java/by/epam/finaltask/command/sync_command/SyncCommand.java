package by.epam.finaltask.command.sync_command;

import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.SyncCommandResponse;
import by.epam.finaltask.model.Role;

import java.util.List;

public interface SyncCommand {

    SyncCommandResponse execute(CommandRequest request);

    List<Role> getAllowedRoles();
}
