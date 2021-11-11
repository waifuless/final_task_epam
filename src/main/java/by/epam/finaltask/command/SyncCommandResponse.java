package by.epam.finaltask.command;

public class SyncCommandResponse {

    private final boolean isRedirect;
    private final String path;

    public SyncCommandResponse(boolean isRedirect, String path) {
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
