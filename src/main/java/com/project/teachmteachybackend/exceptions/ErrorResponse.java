package com.project.teachmteachybackend.exceptions;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class ErrorResponse {
    private String timestamp;
    private String status;
    private String error;
    private String message;

    public void setTimestamp(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        this.timestamp = formatter.format(date);
    }
}
