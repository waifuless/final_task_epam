package by.epam.finaltask.command;

public interface AjaxCommand extends RoledCommand {

    AjaxCommandResponse execute(CommandRequest request) throws Exception;
}
