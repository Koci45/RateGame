package com.KociApp.RateGame.exception.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GameExceptionResponse {

    private int status;

    private String message;

    private long timeStamp;
}