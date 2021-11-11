package by.epam.finaltask.command.async_command;

import by.epam.finaltask.command.AjaxCommandResponse;
import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.RoledCommand;

public interface AjaxCommand extends RoledCommand {

    AjaxCommandResponse execute(CommandRequest request) throws Exception;
}
