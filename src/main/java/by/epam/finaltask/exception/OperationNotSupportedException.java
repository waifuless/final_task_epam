package by.epam.finaltask.exception;

public class OperationNotSupportedException extends RuntimeException {

    public OperationNotSupportedException() {
    }

    public OperationNotSupportedException(String mcg) {
        super(mcg);
    }
}
