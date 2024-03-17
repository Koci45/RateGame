package com.KociApp.RateGame.importGames;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Getter
@Setter
@Component
public class TwitchTokenGetter implements TokenGetter{


    @Override
    public String getAccesToken() throws UnirestException {

        Map<String, String> env = System.getenv(); //getting environment vaiables

        HttpResponse<JsonNode> jsonResponse = Unirest.post("https://id.twitch.tv/oauth2/token" + "?client_id=" + env.get("TWITCHID") + "&client_secret=" + env.get("TWITCHSECRET") +
                        "&grant_type=client_credentials").asJson();

        return jsonResponse.getBody().getObject().getString("access_token");
    }
}
