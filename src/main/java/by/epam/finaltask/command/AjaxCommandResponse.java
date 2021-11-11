package by.epam.finaltask.command;

public class AjaxCommandResponse {
    private final String responseType;
    private final String response;

    public AjaxCommandResponse(String responseType, String response) {
        this.responseType = responseType;
        this.response = response;
    }

    public String getResponseType() {
        return responseType;
    }

    public String getResponse() {
        return response;
    }
}
