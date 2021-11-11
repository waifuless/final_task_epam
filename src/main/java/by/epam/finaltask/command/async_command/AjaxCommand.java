package by.epam.finaltask.command.async_command;

import by.epam.finaltask.command.AjaxCommandResponse;
import by.epam.finaltask.command.CommandRequest;

public interface AjaxCommand {

    AjaxCommandResponse execute(CommandRequest request) throws Exception;
}
