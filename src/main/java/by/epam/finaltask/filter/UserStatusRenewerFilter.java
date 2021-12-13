package by.epam.finaltask.filter;

import by.epam.finaltask.command.UserSessionAttribute;
import by.epam.finaltask.command.handler.HandlerFactory;
import by.epam.finaltask.exception.ServiceCanNotCompleteCommandRequest;
import by.epam.finaltask.model.User;
import by.epam.finaltask.service.ServiceFactory;
import by.epam.finaltask.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebFilter(filterName = "UserStatusRenewerFilter")
public class UserStatusRenewerFilter implements Filter {

    private final static Logger LOG = LoggerFactory.getLogger(UserStatusRenewerFilter.class);

    private final static int DEFAULT_SERVER_ERROR_STATUS = 500;
    private final static String DEFAULT_SERVER_ERROR_MESSAGE = "Unexpected error occurred on server";

    private final UserService userService = ServiceFactory.getFactoryInstance().userService();
    private final HandlerFactory handlerFactory = HandlerFactory.getInstance();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        LOG.debug("In userStatusRenewer filter");

        final HttpServletRequest request = (HttpServletRequest) req;

        HttpSession session = request.getSession(false);
        try {
            if (session != null) {
                Long userId = (Long) session.getAttribute(UserSessionAttribute.USER_ID.name());
                if (userId != null) {
                    Optional<User> optionalUserCurrentState = userService
                            .findUserById(userId);
                    if (optionalUserCurrentState.isPresent()) {
                        updateSession(session, optionalUserCurrentState.get());
                    } else {
                        session.invalidate();
                    }
                }
            }
        } catch (ServiceCanNotCompleteCommandRequest e) {
            LOG.error(e.getMessage(), e);
            sendServerError(request, (HttpServletResponse) res);
            return;
        }
        chain.doFilter(req, res);
    }

    private void sendServerError(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (handlerFactory.isRequestAjax(request)) {
            response.setStatus(DEFAULT_SERVER_ERROR_STATUS);
            try (PrintWriter writer = response.getWriter()) {
                writer.print(DEFAULT_SERVER_ERROR_MESSAGE);
                writer.flush();
            }
        } else {
            response.sendError(DEFAULT_SERVER_ERROR_STATUS, DEFAULT_SERVER_ERROR_MESSAGE);
        }
    }

    private void updateSession(HttpSession session, User user) {
        session.setAttribute(UserSessionAttribute.USER_EMAIL.name(), user.getEmail());
        session.setAttribute(UserSessionAttribute.USER_ROLE.name(), user.getRole());
        session.setAttribute(UserSessionAttribute.USER_BANNED_STATUS.name(), user.isBanned());
    }
}
