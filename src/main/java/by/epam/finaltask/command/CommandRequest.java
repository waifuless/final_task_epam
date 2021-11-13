package by.epam.finaltask.command;

import by.epam.finaltask.command.sync_command.SyncCommandFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class CommandRequest {

    private final HttpServletRequest request;

    private final String commandPathTemplate;

    public CommandRequest(HttpServletRequest request) {
        this.request = request;
        commandPathTemplate = request.getContextPath() + request.getServletPath() + "?command=%s";
    }

    public String createCommandPath(SyncCommandFactory.SyncCommandVariant syncCommandVariant) {
        return String.format(commandPathTemplate, syncCommandVariant.getCommandName());
    }

    public HttpSession createSession() {
        return request.getSession(true);
    }

    public HttpSession getSession() {
        return request.getSession(false);
    }

    public String getParameter(String s) {
        return request.getParameter(s);
    }

    public String[] getParameterValues(String s) {
        return request.getParameterValues(s);
    }

    public Map<String, String[]> getParameterMap() {
        return request.getParameterMap();
    }

    public void setAttribute(String name, Object value) {
        request.setAttribute(name, value);
    }

    public Object getAttribute(String name) {
        return request.getAttribute(name);
    }

    public String getContextPath() {
        return request.getContextPath();
    }

    public String getServletPath() {
        return request.getServletPath();
    }

    public Collection<Part> getParts() throws IOException, ServletException {
        return request.getParts();
    }

    public Part getPart(String s) throws IOException, ServletException {
        return request.getPart(s);
    }
}
