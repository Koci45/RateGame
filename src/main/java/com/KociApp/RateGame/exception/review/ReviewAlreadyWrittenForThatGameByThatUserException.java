package com.KociApp.RateGame.exception.review;

public class ReviewAlreadyWrittenForThatGameByThatUserException extends RuntimeException{
    public ReviewAlreadyWrittenForThatGameByThatUserException(String message) {
        super(message);
    }
}
