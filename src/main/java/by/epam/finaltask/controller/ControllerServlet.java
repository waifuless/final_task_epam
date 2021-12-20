package by.epam.finaltask.controller;

import by.epam.finaltask.command.handler.CommandHandler;
import by.epam.finaltask.command.handler.HandlerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@MultipartConfig
@WebServlet(name = "ControllerServlet", value = "/ControllerServlet")
public class ControllerServlet extends HttpServlet {

    private final static Logger LOG = LoggerFactory.getLogger(ControllerServlet.class);

    private final HandlerFactory handlerFactory = HandlerFactory.getInstance();

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
            CommandHandler commandHandler = handlerFactory.find(request);
            commandHandler.handle(request, response);
        } catch (Exception ex) {
            LOG.warn(ex.getMessage(), ex);
            request.setAttribute("errorMessage", ex.getMessage());
            request.getRequestDispatcher(by.epam.finaltask.controller.PagePath.ERROR.getPath())
                    .forward(request, response);
        }
    }
}
