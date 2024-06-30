package com.retail.rewards.model.exception;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Data
@Builder
@ToString(of = "message")
@EqualsAndHashCode(callSuper = false)
public class AppException extends RuntimeException{

    private String message;
    private HttpStatus httpStatus;

    public AppException() {
        super();
    }

    public AppException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }

}