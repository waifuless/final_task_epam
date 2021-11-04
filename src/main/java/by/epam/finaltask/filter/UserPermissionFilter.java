package by.epam.finaltask.filter;

import by.epam.finaltask.command.Command;
import by.epam.finaltask.command.CommandFactory;
import by.epam.finaltask.controller.PagePath;
import by.epam.finaltask.controller.UserSessionAttribute;
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

@WebFilter(filterName = "UserPermissionFilter")
public class UserPermissionFilter implements Filter {

    private final static Logger LOG = LoggerFactory.getLogger(UserPermissionFilter.class);
    private final static String FORBIDDEN_MCG = "forbidden {} to access {} command";
    private final static int FORBIDDEN_STATUS_CODE = 403;
    private final static String BAD_REQUEST_MCG = "Command {} does not exist";
    private final static int BAD_REQUEST_STATUS_CODE = 400;
    private final CommandFactory commandFactory = CommandFactory.getInstance();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        final Role userRole = getCurrentRole(request);

        String commandName = request.getParameter("command");
        Optional<Command> optionalCommand = commandFactory.findCommand(commandName);

        if(!optionalCommand.isPresent() ){
            LOG.warn(BAD_REQUEST_MCG, commandName);
            response.sendError(BAD_REQUEST_STATUS_CODE);
            return;
        }
        if (!optionalCommand.get().getAllowedRoles().contains(userRole)) {
            LOG.warn(FORBIDDEN_MCG, userRole, optionalCommand.get());
            response.sendError(FORBIDDEN_STATUS_CODE);
            return;
        }
        chain.doFilter(req, res);
    }

    private Role getCurrentRole(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return Role.NOT_AUTHORIZED;
        }
        Role role = (Role) session.getAttribute(UserSessionAttribute.USER_ROLE.name());
        return role != null ? role : Role.NOT_AUTHORIZED;
    }
}
