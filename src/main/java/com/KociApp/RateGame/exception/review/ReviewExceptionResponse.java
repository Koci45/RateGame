package com.KociApp.RateGame.exception.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewExceptionResponse {
    private int status;

    private String message;

    private long timeStamp;
}
