package com.KociApp.RateGame.exception.game;

import com.KociApp.RateGame.exception.user.UserAlreadyExistsException;
import com.KociApp.RateGame.exception.user.UserAlreadyExistsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GameExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<GameNotFoundResponse> handleException(GameNotFoundException exc){

        GameNotFoundResponse error = new GameNotFoundResponse(HttpStatus.NOT_FOUND.value(), exc.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
