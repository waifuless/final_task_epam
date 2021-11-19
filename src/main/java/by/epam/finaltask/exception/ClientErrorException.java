package by.epam.finaltask.exception;

/**
 *This exception is intended for situations in which the error seems to have been caused by the client.
 */
public class ClientErrorException extends Exception{

    private final int errorStatus;

    public ClientErrorException(int errorStatus, String message) {
        super(message);
        this.errorStatus = errorStatus;
    }

    public ClientErrorException(int errorStatus, String message, Throwable cause) {
        super(message, cause);
        this.errorStatus = errorStatus;
    }

    public ClientErrorException(ClientError clientError){
        super(clientError.getMessage());
        this.errorStatus = clientError.getErrorStatus();
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
