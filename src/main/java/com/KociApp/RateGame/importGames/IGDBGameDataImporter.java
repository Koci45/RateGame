package com.KociApp.RateGame.importGames;

import com.KociApp.RateGame.game.Game;
import com.KociApp.RateGame.game.GameRepository;
import com.KociApp.RateGame.game.cover.Cover;
import com.KociApp.RateGame.game.cover.CoverRepository;
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

@RequiredArgsConstructor
@Component
public class IGDBGameDataImporter implements GameDataImporter{

    private final GenreRepository genreRepository;
    private final PlatformRepository platformRepository;
    private final GameRepository gameRepository;
    private final CoverRepository coverRepository;

    /**
     * This functions checks the latest creation_time of game that we have in database and then asks
     * the igdb database if they have any games after that creation_date, if so it downloads them,
     * creation_date doesn't mean the creation_date of the game itself, rather it means when it was first added to
     * igdb database.
     * there is limit in igdb database for max 500 record per request so this function may need to be called multiple times, (probably only at firs time)
     * @param accesToken Twitch access token
     * @return number of games imported
     * @throws UnirestException
     * @throws IOException
     */
    public int importGamesFromIGDB (String accesToken) throws UnirestException, IOException {

        Date latestUpdate = gameRepository.findLatestCreatedAt();
        int gameCounter = 0;

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

        if(jsonResponse.getStatus() != 200){
            throw new RuntimeException("Could not download games from IGDB, response code:" + jsonResponse.getStatus());
        }

        if(jsonResponse.getBody().toString().length() == 2){
            return 0;
        }

        //saving response from igdb as java objects
        ObjectMapper mapper = new ObjectMapper();
        GameResponseHolder[] games = mapper.readValue(jsonResponse.getRawBody(), GameResponseHolder[].class);

        //saving every downloaded game to database after translating from igdb format to database format object
        for(GameResponseHolder gameResponseHolder : games){
            gameRepository.save(translateFromIGDBToDataBaseFormat(gameResponseHolder));
            gameCounter++;
        }

        return gameCounter;
    }

    /**
     * This method imports Genres table from igdb so they can be assigned
     * to imported games from igdb
     * @param accesToken Twitch acces token
     * @throws UnirestException
     * @throws IOException
     */
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

    /**
     * this method imports Platfrorms table from igdb
     * so they can be assigned to imported games from igdb
     * @param accesToken
     * @throws UnirestException
     * @throws IOException
     */
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

    /**
     * this method tranlates IGDB response object format to database object format
     * @param gameResponseHolder Response object from IGDB
     * @return
     */
    public Game translateFromIGDBToDataBaseFormat(GameResponseHolder gameResponseHolder){
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

        return game;
    }

    public int importCoversFromIGDB(String accesToken) throws UnirestException, IOException{

        Integer highestGameId = coverRepository.findHighestGameId();
        int gameId = highestGameId == null ? 0 : highestGameId;

        HttpResponse<JsonNode> jsonResponse = Unirest.post("https://api.igdb.com/v4/covers")
                .header("Client-ID", "6idsg519ob3dp93vzqhbgdxfjug2rg")
                .header("Authorization", "Bearer " + accesToken)
                .header("Accept", "application/json")
                .body("fields id, game, url;" +
                        "limit 500;" +
                        "where game > " + gameId + ";" +
                        "sort game asc;")
                .asJson();

        ObjectMapper mapper = new ObjectMapper();
        Cover[] covers = mapper.readValue(jsonResponse.getRawBody(), Cover[].class);

        coverRepository.saveAll(Arrays.asList(covers));// saving all to the database
        return  covers.length;
    }

}
