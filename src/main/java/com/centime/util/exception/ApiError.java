package com.centime.util.exception;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class ApiError implements Serializable {

    private static final long serialVersionUID = 6916217858899431308L;
    private Integer status;
    private HttpStatus httpStatus;
    private String message;
    private String developerMessage;

    public ApiError(Integer httpStatus, String message) {
        this.message = message;
        this.status = httpStatus;
    }

    public ApiError(HttpStatus httpStatus, String message, String developerMessage) {
        super();
        this.message = message;
        this.status = httpStatus.value();
        this.httpStatus = httpStatus;
        this.developerMessage = developerMessage;
    }

    public ApiError(HttpStatus httpStatus, String message) {
        super();
        this.message = message;
        this.status = httpStatus.value();
        this.httpStatus = httpStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public void setDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
