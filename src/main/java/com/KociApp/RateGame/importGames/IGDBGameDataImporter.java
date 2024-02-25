package com.KociApp.RateGame.importGames;

import com.KociApp.RateGame.game.Game;
import com.KociApp.RateGame.game.GameRepository;
import com.KociApp.RateGame.game.genre.Genre;
import com.KociApp.RateGame.game.genre.GenreRepository;
import com.KociApp.RateGame.game.platform.Platform;
import com.KociApp.RateGame.game.platform.PlatformRepository;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class IGDBGameDataImporter implements GameDataImporter{

    private final GenreRepository genreRepository;
    private final PlatformRepository platformRepository;
    private final GameRepository gameRepository;

    public boolean importGamesFromIGDB (String accesToken) throws UnirestException, IOException {

        Date latestUpdate = gameRepository.findLatestCreatedAt();

        if(latestUpdate == null){
            latestUpdate = new Date(0);//setting it to the lowest possible date if the table was empty
        }

        HttpResponse<JsonNode> jsonResponse = Unirest.post("https://api.igdb.com/v4/games")
                .header("Client-ID", "6idsg519ob3dp93vzqhbgdxfjug2rg")
                .header("Authorization", "Bearer " + accesToken)
                .header("Accept", "application/json")
                .body("fields id,name,cover,created_at,first_release_date,genres,platforms;" +
                        "where created_at > " + latestUpdate.getTime()/1000 + ";" +
                        "sort created_at asc;" +
                        "limit 500;")
                .asJson();

        if(jsonResponse.getBody().toString().length() == 2){
            return false;
        }

        ObjectMapper mapper = new ObjectMapper();
        GameResponseHolder[] games = mapper.readValue(jsonResponse.getRawBody(), GameResponseHolder[].class);

        //saving every downloaded game to database after translating to database format object
        for(GameResponseHolder gameResponseHolder : games){
            Game game = new Game();
            game.setId(gameResponseHolder.getId());
            game.setTitle(gameResponseHolder.getName());
            game.setCover(gameResponseHolder.getCover());
            game.setReleaseDate(new Date(gameResponseHolder.getFirst_release_date() * 1000));
            game.setCreatedAt(new Date(gameResponseHolder.getCreated_at() * 1000));

            //Setting genres
            String genres = "";
            if(gameResponseHolder.getGenres() != null)
            {
                for(int id : gameResponseHolder.getGenres()){
                    genres += genreRepository.findById(id).get().getName() + "; ";
                }
            }

            game.setGenres(genres);

            //Setting platforms
            String platforms = "";
            if(gameResponseHolder.getPlatforms() != null){
                for(int id : gameResponseHolder.getPlatforms()){
                    platforms += platformRepository.findById(id).get().getName() + "; ";
                }
            }
            game.setPlatforms(platforms);

            gameRepository.save(game);
        }

        return true;
    }

    public void importGenresFromIGDB(String accesToken) throws UnirestException, IOException {
        HttpResponse<JsonNode> jsonResponse = Unirest.post("https://api.igdb.com/v4/genres")
                .header("Client-ID", "6idsg519ob3dp93vzqhbgdxfjug2rg")
                .header("Authorization", "Bearer " + accesToken)
                .header("Accept", "application/json")
                .body("fields id, name;" +
                        "limit 500;")
                .asJson();

        ObjectMapper mapper = new ObjectMapper();
        Genre[] genres = mapper.readValue(jsonResponse.getRawBody(), Genre[].class);

        genreRepository.saveAll(Arrays.asList(genres));// saving all to the database

        System.out.println(jsonResponse.getBody().toString());
    }

    public void importPlatformsFromIGDB(String accesToken) throws UnirestException, IOException {
        HttpResponse<JsonNode> jsonResponse = Unirest.post("https://api.igdb.com/v4/platforms")
                .header("Client-ID", "6idsg519ob3dp93vzqhbgdxfjug2rg")
                .header("Authorization", "Bearer " + accesToken)
                .header("Accept", "application/json")
                .body("fields id, name;" +
                        "limit 500;")
                .asJson();

        ObjectMapper mapper = new ObjectMapper();
        Platform[] platforms = mapper.readValue(jsonResponse.getRawBody(), Platform[].class);

        platformRepository.saveAll(Arrays.asList(platforms));// saving all to the database

        System.out.println(jsonResponse.getBody().toString());
    }
}
