package com.KociApp.RateGame.importGames;

import com.mashape.unirest.http.exceptions.UnirestException;

public interface TokenGetter {

    public String getAccesToken() throws UnirestException;
}
