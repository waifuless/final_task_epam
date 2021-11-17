package by.epam.finaltask.filter;

import by.epam.finaltask.command.RoledCommand;
import by.epam.finaltask.command.UserSessionAttribute;
import by.epam.finaltask.command.async_command.AjaxCommandFactory;
import by.epam.finaltask.command.handler.HandlerFactory;
import by.epam.finaltask.command.sync_command.SyncCommandFactory;
import by.epam.finaltask.model.Role;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;

@WebFilter(filterName = "UserPermissionFilter")
public class UserPermissionFilter implements Filter {

    private final static Logger LOG = LoggerFactory.getLogger(UserPermissionFilter.class);
    private final static String BAD_REQUEST_ERROR_MESSAGE = "Unknown command received %s";
    private final static String FORBIDDEN_ERROR_MESSAGE = "Forbidden to access %s";
    private final static String FORBIDDEN_LOG_MCG = "forbidden {} to access {} command";
    private final static String BAD_REQUEST_LOG_MCG = "Command {} does not exist";
    private final SyncCommandFactory syncCommandFactory = SyncCommandFactory.getInstance();
    private final AjaxCommandFactory ajaxCommandFactory = AjaxCommandFactory.getInstance();
    private final HandlerFactory handlerFactory = HandlerFactory.getInstance();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        LOG.debug("In permission filter");
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        final Role userRole = getCurrentRole(request);

        String commandName = request.getParameter("command");
        LOG.debug("Command: {}", commandName);
        Optional<RoledCommand> roledCommand = findCommand(commandName, request);

        if (!roledCommand.isPresent()) {
            LOG.warn(BAD_REQUEST_LOG_MCG, commandName);
            response.sendError(SC_BAD_REQUEST, String.format(BAD_REQUEST_ERROR_MESSAGE, commandName));
            return;
        }
        if (!roledCommand.get().getAllowedRoles().contains(userRole)) {
            LOG.warn(FORBIDDEN_LOG_MCG, userRole, roledCommand.get());
            response.sendError(SC_FORBIDDEN, String.format(FORBIDDEN_ERROR_MESSAGE, commandName));
            return;
        }
        chain.doFilter(req, res);
    }

    private Role getCurrentRole(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            request.getSession(true).setAttribute(UserSessionAttribute.USER_ROLE.name(), Role.NOT_AUTHORIZED);
            return Role.NOT_AUTHORIZED;
        }
        Role role = (Role) session.getAttribute(UserSessionAttribute.USER_ROLE.name());
        return role != null ? role : Role.NOT_AUTHORIZED;
    }

    private Optional<RoledCommand> findCommand(String commandName, HttpServletRequest request) {
        if (handlerFactory.isRequestAjax(request)) {
            return Optional.ofNullable(ajaxCommandFactory.findCommand(commandName).orElse(null));
        } else {
            return Optional.ofNullable(syncCommandFactory.findCommand(commandName).orElse(null));
        }
    }
}
