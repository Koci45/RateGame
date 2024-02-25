package com.KociApp.RateGame.importGames;

import com.mashape.unirest.http.exceptions.UnirestException;

public interface GameDataImporter {

    public void importGamesFromIGDB (String accesToken) throws UnirestException;
}
