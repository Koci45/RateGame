package com.KociApp.RateGame.exception.review;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ReviewExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ReviewExceptionResponse> handleException(ReviewAlreadyWrittenForThatGameByThatUserException exc){

        ReviewExceptionResponse error = new ReviewExceptionResponse(HttpStatus.CONFLICT.value(), exc.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

}
