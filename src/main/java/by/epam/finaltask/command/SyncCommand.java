package by.epam.finaltask.command;

import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;

public interface SyncCommand extends RoledCommand {

    SyncCommandResponse execute(CommandRequest request) throws ClientErrorException, ServiceCanNotCompleteCommandRequest;
}
