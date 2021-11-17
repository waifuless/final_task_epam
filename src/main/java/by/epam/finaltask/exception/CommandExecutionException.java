package by.epam.finaltask.exception;

public class CommandExecutionException extends Exception{

    private final static int DEFAULT_SERVER_EXCEPTION = 500;

    private final int errorStatus;

    public CommandExecutionException() {
        errorStatus = DEFAULT_SERVER_EXCEPTION;
    }

    public CommandExecutionException(int errorStatus, String message) {
        super(message);
        this.errorStatus = errorStatus;
    }

    public CommandExecutionException(Throwable cause) {
        super(cause);
        errorStatus = DEFAULT_SERVER_EXCEPTION;
    }

    public CommandExecutionException(int errorStatus, String message, Throwable cause) {
        super(message, cause);
        this.errorStatus = errorStatus;
    }

    public CommandExecutionException(CommandError commandError){
        super(commandError.getMessage());
        this.errorStatus = commandError.getErrorStatus();
    }

    public int getErrorStatus() {
        return errorStatus;
    }

    @Override
    public String toString() {
        return "CommandExecutionException{" +
                super.toString()+
                "[errorStatus=" + errorStatus +
                "]}";
    }
}
