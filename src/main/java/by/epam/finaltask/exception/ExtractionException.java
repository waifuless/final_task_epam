package by.epam.finaltask.exception;

public class ExtractionException extends RuntimeException {

    public ExtractionException() {
    }

    public ExtractionException(String mcg) {
        super(mcg);
    }

    public ExtractionException(String mcg, Throwable throwable) {
        super(mcg, throwable);
    }
}
