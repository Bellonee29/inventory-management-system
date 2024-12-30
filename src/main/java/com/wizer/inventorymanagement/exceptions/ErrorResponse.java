package com.wizer.inventorymanagement.exceptions;


import lombok.Data;


public class ErrorResponse {
    private String message;
    private int status;

    // Constructors, getters, and setters
    public ErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
