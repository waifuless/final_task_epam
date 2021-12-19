package by.epam.finaltask.exception;

/**
 * Enum with common client errors
 */
public enum ClientError {
    EMPTY_ARGUMENTS(400, "One or more arguments are empty"),
    INVALID_NUMBER(400, "Invalid number format"),
    INVALID_IMAGE(400, "Image is invalid"),
    INVALID_ARGUMENTS(400, "One or more arguments are invalid"),
    NOT_FOUND(404, "Resource not found"),
    ENTITY_ALREADY_EXISTS(409, "Entity already exists"),
    LOT_NOT_RUNNING(409, "Lot is not running"),
    FORBIDDEN(403, "Forbidden to execute command"),
    BID_AMOUNT_INVALID(409, "Bid amount should be bigger than init price in forward auction and " +
            "smaller in reverse");

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
