package by.epam.finaltask.exception;

public class InvalidArgumentException extends RuntimeException {

    public InvalidArgumentException() {
    }

    public InvalidArgumentException(String mcg) {
        super(mcg);
    }
}
