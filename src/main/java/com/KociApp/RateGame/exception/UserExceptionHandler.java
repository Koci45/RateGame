package com.KociApp.RateGame.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<UserAlreadyExistsResponse> handleException(UserAlreadyExistsException exc){

        UserAlreadyExistsResponse error = new UserAlreadyExistsResponse(HttpStatus.CONFLICT.value(), exc.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}
