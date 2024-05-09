package com.project.teachmteachybackend.exceptions;


import com.project.teachmteachybackend.dto.error.ErrorResponse;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;
@Data
public class BaseErrorException extends RuntimeException {

    private HttpStatus status;
    private String error;
    private String message;

    public BaseErrorException(String error, String message,  HttpStatus status) {
        this.error = error;
        this.message = message;
        this.status = status;
    }

    // getters and setters
}

