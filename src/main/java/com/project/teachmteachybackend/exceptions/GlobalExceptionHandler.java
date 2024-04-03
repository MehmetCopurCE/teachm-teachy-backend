package com.project.teachmteachybackend.exceptions;

import com.project.teachmteachybackend.dto.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex) {
        // Burada istediğiniz hata detaylarını ve HTTP durum kodunu döndürebilirsiniz.
        return new ResponseEntity<>("Bir hata oluştu: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        errorResponse.setError(ex.getClass().getSimpleName());
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        errorResponse.setError(ex.getClass().getSimpleName());
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        // ErrorResponse nesnesini oluşturun
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(String.valueOf(HttpStatus.NOT_FOUND.value()));
        errorResponse.setError(ex.getClass().getSimpleName());
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FriendRequestException.class)
    public ResponseEntity<ErrorResponse> handleFriendRequestException(FriendRequestException ex) {
        // ErrorResponse nesnesini oluşturun
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(String.valueOf(HttpStatus.FOUND.value()));
        errorResponse.setError(ex.getClass().getSimpleName());
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FOUND);
    }

    @ExceptionHandler(UsernameInUseException.class)
    public ResponseEntity<ErrorResponse> handleUsernameInUseException(UsernameInUseException ex) {
        // ErrorResponse nesnesini oluşturun
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        errorResponse.setError(ex.getClass().getSimpleName());
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExistEmailException.class)
    public ResponseEntity<ErrorResponse> handleExistEmailException(ExistEmailException ex) {
        // ErrorResponse nesnesini oluşturun
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        errorResponse.setError(ex.getClass().getSimpleName());
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


}