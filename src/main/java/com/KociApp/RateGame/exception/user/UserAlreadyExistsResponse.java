package com.KociApp.RateGame.exception.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserAlreadyExistsResponse {

    private int status;

    private String message;

    private long timeStamp;
}
