package by.epam.finaltask.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Map;

public class CommandRequest {

    private final HttpServletRequest request;

    public CommandRequest(HttpServletRequest request) {
        this.request = request;
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
}
