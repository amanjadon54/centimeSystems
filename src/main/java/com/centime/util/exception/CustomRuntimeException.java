package com.centime.util.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomRuntimeException extends RuntimeException {
    private String message;
    private Integer status;
    private String developerMsg;
    private String logId;

    public CustomRuntimeException(String message, Integer status, String developerMsg) {
        this.message = message;
        this.status = status;
        this.developerMsg = developerMsg;
    }

    public CustomRuntimeException(String message, Integer status, String developerMsg, String logId) {
        this.message = message;
        this.status = status;
        this.developerMsg = developerMsg;
        this.logId = logId;
    }
}
