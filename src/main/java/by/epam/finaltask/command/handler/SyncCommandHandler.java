package by.epam.finaltask.command.handler;

import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.SyncCommandResponse;
import by.epam.finaltask.command.sync_command.SyncCommand;
import by.epam.finaltask.command.sync_command.SyncCommandFactory;
import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.exception.InvalidArgumentException;
import by.epam.finaltask.exception.OperationNotSupportedException;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class SyncCommandHandler implements CommandHandler {

    private final static Logger LOG = LogManager.getLogger(SyncCommandHandler.class);
    private final static String OPERATION_NOT_FOUND_MCG = "Operation %s not found";
    private final static String SERVICE_ERROR_MCG = "An error occurred during service execution";
    private final static String UNKNOWN_ERROR_MCG = "An unknown error occurred on server";

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
        } catch (ClientErrorException ex) {
            LOG.warn(ex.getMessage(), ex);
            response.sendError(ex.getErrorStatus(), ex.getMessage());
        } catch (ServiceCanNotCompleteCommandRequest ex){
            LOG.warn(ex.getMessage(), ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, SERVICE_ERROR_MCG);
        } catch (Exception ex){
            LOG.warn(ex.getMessage(), ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, UNKNOWN_ERROR_MCG);
        }
    }
}
