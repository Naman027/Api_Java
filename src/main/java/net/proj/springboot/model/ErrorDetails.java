package net.proj.springboot.model;

public class ErrorDetails {
    private String message;
    private String statusCode;

    public ErrorDetails(String message) {
        this.message = message;
    }
    public ErrorDetails(String message, String statusCode){
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}



