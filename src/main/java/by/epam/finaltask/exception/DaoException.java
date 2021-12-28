package by.epam.finaltask.exception;

public class DaoException extends RuntimeException{

    public DaoException() {
    }

    public DaoException(String mcg) {
        super(mcg);
    }

    public DaoException(Throwable throwable) {
        super(throwable);
    }

    public DaoException(String mcg, Throwable throwable) {
        super(mcg, throwable);
    }
}
