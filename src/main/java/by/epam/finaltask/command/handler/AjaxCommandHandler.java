package by.epam.finaltask.command.handler;

import by.epam.finaltask.command.AjaxCommandResponse;
import by.epam.finaltask.command.CommandRequest;
import by.epam.finaltask.command.async_command.AjaxCommand;
import by.epam.finaltask.command.async_command.AjaxCommandFactory;
import by.epam.finaltask.exception.ClientErrorException;
import by.epam.finaltask.exception.OperationNotSupportedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;

public class AjaxCommandHandler implements CommandHandler {

    private final static Logger LOG = LogManager.getLogger(AjaxCommandHandler.class);
    private final static String OPERATION_NOT_FOUND_MCG = "Operation %s not found";
    private final static String UNKNOWN_ERROR_MCG = "An unknown error occurred on server";

    private final AjaxCommandFactory ajaxCommandFactory = AjaxCommandFactory.getInstance();

    AjaxCommandHandler() {
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String requestedCommand = request.getParameter("command");
            AjaxCommand ajaxCommand = ajaxCommandFactory.findCommand(requestedCommand)
                    .orElseThrow(() -> new OperationNotSupportedException
                            (String.format(OPERATION_NOT_FOUND_MCG, requestedCommand)));
            AjaxCommandResponse ajaxCommandResponse = ajaxCommand.execute(new CommandRequest(request));

            response.setContentType(ajaxCommandResponse.getResponseType());
            try (PrintWriter writer = response.getWriter()) {
                writer.write(ajaxCommandResponse.getResponse());
            }
        } catch (ClientErrorException ex) {
            LOG.warn(ex.getMessage(), ex);
            response.setStatus(ex.getErrorStatus());
            try (PrintWriter writer = response.getWriter()) {
                writer.print(ex.getMessage());
                writer.flush();
            }
        } catch (Exception ex) {
            LOG.warn(ex.getMessage(), ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter writer = response.getWriter()) {
                writer.print(UNKNOWN_ERROR_MCG);
                writer.flush();
            }
        }
    }
}
