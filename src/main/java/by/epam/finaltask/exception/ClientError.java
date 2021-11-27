package by.epam.finaltask.exception;

/**
 * Enum with common client errors
 */
public enum ClientError {
    EMPTY_ARGUMENTS(400, "One or more arguments are empty"),
    INVALID_NUMBER(400, "Invalid number format"),
    INVALID_IMAGE(400, "Image is invalid"),
    NOT_FOUND(404, "Resource not found"),
    ENTITY_ALREADY_EXISTS(409, "Entity already exists");

    private final int errorStatus;
    private final String message;

    ClientError(int errorStatus, String message) {
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
