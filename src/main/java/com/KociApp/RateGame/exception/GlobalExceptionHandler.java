package com.KociApp.RateGame.exception;

import com.KociApp.RateGame.exception.game.GameNotFoundException;
import com.KociApp.RateGame.exception.game.GameNotFoundResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<GlobalExceptionResponse> handleException(MethodArgumentTypeMismatchException exc){

        GlobalExceptionResponse error = new GlobalExceptionResponse(HttpStatus.BAD_REQUEST.value(), exc.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
//MethodArgumentTypeMismatchException