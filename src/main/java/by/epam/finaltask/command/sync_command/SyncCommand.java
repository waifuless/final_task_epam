package by.epam.finaltask.command.sync_command;

import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.RoledCommand;
import by.epam.finaltask.command.SyncCommandResponse;

public interface SyncCommand extends RoledCommand {

    SyncCommandResponse execute(CommandRequest request);
}
