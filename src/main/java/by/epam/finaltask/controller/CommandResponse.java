package by.epam.finaltask.controller;

public class CommandResponse {

    private final boolean isRedirect;
    private final String path;

    public CommandResponse(boolean isRedirect, String path) {
        this.isRedirect = isRedirect;
        this.path = path;
    }

    public boolean isRedirect() {
        return isRedirect;
    }

    public String getPath() {
        return path;
    }
}
