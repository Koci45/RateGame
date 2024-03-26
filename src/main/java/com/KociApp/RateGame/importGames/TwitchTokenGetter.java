package com.KociApp.RateGame.importGames;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Getter
@Setter
@Component
public class TwitchTokenGetter implements TokenGetter{

    private final TwitchService twitchService;

    @Override
    public String getAccesToken() throws UnirestException {

        HttpResponse<JsonNode> jsonResponse = twitchService.getAccesToken();

        if(jsonResponse.getStatus() != 200){
            throw new RuntimeException("could not get twitch acces token, response code-" + jsonResponse.getStatus());
        }

        return jsonResponse.getBody().getObject().getString("access_token");
    }
}
