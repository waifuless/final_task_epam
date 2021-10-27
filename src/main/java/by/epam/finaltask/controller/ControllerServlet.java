package by.epam.finaltask.controller;

import by.epam.finaltask.connection_pool.CommonConnectionPool;
import by.epam.finaltask.connection_pool.ConnectionPool;
import by.epam.finaltask.exception.DataSourceDownException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet(name = "ControllerServlet", value = "/ControllerServlet")
public class ControllerServlet extends HttpServlet {

    private final static Logger LOG = LoggerFactory.getLogger(ControllerServlet.class);

    private final static String DESTROY_EXCEPTION_MCG = "exception while destroying servlet";
    private final static String INIT_EXCEPTION_MCG = "exception while initializing servlet";

    @Override
    public void init() throws ServletException {
        try {
            ConnectionPool.getInstance(); //init connection pool
        } catch (Exception e) {
            LOG.error(INIT_EXCEPTION_MCG, e);
            throw new ServletException(e);
        }
    }

    @Override
    public void destroy() {
        try {
            ConnectionPool.getInstance().close();
        } catch (Exception e) {
            LOG.error(DESTROY_EXCEPTION_MCG, e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
