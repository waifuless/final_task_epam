package by.epam.finaltask.exception;

public class ServiceCanNotCompleteCommandRequest extends Exception {

    public ServiceCanNotCompleteCommandRequest() {
    }

    public ServiceCanNotCompleteCommandRequest(String mcg) {
        super(mcg);
    }

    public ServiceCanNotCompleteCommandRequest(Throwable throwable) {
        super(throwable);
    }

    public ServiceCanNotCompleteCommandRequest(String mcg, Throwable throwable) {
        super(mcg, throwable);
    }
}
