package by.epam.finaltask.exception;

public enum CommandError {
    EMPTY_ARGUMENTS(400,"One or more arguments are empty"),
    INVALID_NUMBER(400, "Invalid number format"),
    INVALID_IMAGE(400, "Image is invalid"),
    NOT_FOUND(404, "Resource not found");

    private final int errorStatus;
    private final String message;

    CommandError(int errorStatus, String message) {
        this.errorStatus = errorStatus;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getErrorStatus() {
        return errorStatus;
    }
}
