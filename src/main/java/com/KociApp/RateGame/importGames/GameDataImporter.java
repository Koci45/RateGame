package com.KociApp.RateGame.importGames;

import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;

public interface GameDataImporter {

    public boolean importGamesFromIGDB (String accesToken) throws UnirestException, IOException;

}
