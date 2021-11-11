package by.epam.finaltask.command.handler;

import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.SyncCommandResponse;
import by.epam.finaltask.command.sync_command.SyncCommand;
import by.epam.finaltask.command.sync_command.SyncCommandFactory;
import by.epam.finaltask.exception.InvalidArgumentException;
import by.epam.finaltask.exception.OperationNotSupportedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class SyncCommandHandler implements CommandHandler {

    private final static Logger LOG = LogManager.getLogger(SyncCommandHandler.class);
    private final static String OPERATION_NOT_FOUND_MCG = "Operation %s not found";

    private final SyncCommandFactory syncCommandFactory = SyncCommandFactory.getInstance();

    SyncCommandHandler(){}

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String requestedCommand = request.getParameter("command");
            if (requestedCommand == null) {
                throw new InvalidArgumentException("command does not exist");
            }
            SyncCommand syncCommand = syncCommandFactory.findCommand(requestedCommand)
                    .orElseThrow(() -> new OperationNotSupportedException
                            (String.format(OPERATION_NOT_FOUND_MCG, requestedCommand)));
            SyncCommandResponse syncCommandResponse = syncCommand.execute(new CommandRequest(request));
            if (syncCommandResponse.isRedirect()) {
                response.sendRedirect(syncCommandResponse.getPath());
            } else {
                request.getRequestDispatcher(syncCommandResponse.getPath()).forward(request, response);
            }
        } catch (Exception ex) {
            LOG.warn(ex.getMessage(), ex);
            request.setAttribute("errorMessage", ex.getMessage());
            request.getRequestDispatcher(by.epam.finaltask.controller.PagePath.ERROR.getPath())
                    .forward(request, response);
        }
    }
}
