package by.epam.finaltask.exception;

public class DataSourceDownException extends Error {

    public DataSourceDownException() {
    }

    public DataSourceDownException(String str) {
        super(str);
    }

    public DataSourceDownException(String str, Throwable cause) {
        super(str, cause);
    }

    public DataSourceDownException(Throwable cause) {
        super(cause);
    }
}
