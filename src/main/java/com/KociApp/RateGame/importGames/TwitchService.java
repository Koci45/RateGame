package com.KociApp.RateGame.importGames;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@AllArgsConstructor
@Service
public class TwitchService {

    public HttpResponse<JsonNode> getAccesToken() throws UnirestException {
        Map<String, String> env = System.getenv(); //getting environment vaiables

        return Unirest.post("https://id.twitch.tv/oauth2/token" + "?client_id=" + env.get("TWITCHID") + "&client_secret=" + env.get("TWITCHSECRET") +
                "&grant_type=client_credentials").asJson();
    }
}
