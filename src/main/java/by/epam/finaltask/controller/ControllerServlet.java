package by.epam.finaltask.controller;

import by.epam.finaltask.command.Command;
import by.epam.finaltask.command.CommandFactory;
import by.epam.finaltask.exception.InvalidArgumentException;
import by.epam.finaltask.exception.OperationNotSupportedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebServlet(name = "ControllerServlet", value = "/ControllerServlet")
public class ControllerServlet extends HttpServlet {

    private final static Logger LOG = LogManager.getLogger(ControllerServlet.class);

    private final CommandFactory commandFactory = CommandFactory.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String requestedCommand = request.getParameter("command");
            if (requestedCommand == null) {
                throw new InvalidArgumentException("command does not exist");
            }
            Command command = commandFactory.findCommand(requestedCommand)
                    .orElseThrow(OperationNotSupportedException::new);
            CommandResponse commandResponse = command.execute(new CommandRequest(request));
            if (commandResponse.isRedirect()) {
                response.sendRedirect(commandResponse.getPath());
            } else {
                request.getRequestDispatcher(commandResponse.getPath()).forward(request, response);
            }
        } catch (Exception ex) {
            LOG.warn(ex.getMessage(), ex);
            request.setAttribute("errorMessage", ex.getMessage());
            request.getRequestDispatcher(by.epam.finaltask.controller.PagePath.ERROR.getPath()).forward(request, response);
        }
    }
}
