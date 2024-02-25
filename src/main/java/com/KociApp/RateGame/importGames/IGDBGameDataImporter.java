package com.KociApp.RateGame.importGames;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class IGDBGameDataImporter implements GameDataImporter{

    public void importGamesFromIGDB (String accesToken) throws UnirestException {
        HttpResponse<JsonNode> jsonResponse = Unirest.post("https://api.igdb.com/v4/games")
                .header("Client-ID", "6idsg519ob3dp93vzqhbgdxfjug2rg")
                .header("Authorization", "Bearer " + accesToken)
                .header("Accept", "application/json")
                .body("fields category,name,cover,created_at,first_release_date,genres,involved_companies,platforms; where id > 1;where id < 10; limit 10;")
                .asJson();

        System.out.println(jsonResponse.getBody().toString());
    }
}
