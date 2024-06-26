package com.KociApp.RateGame.tasks;

import com.KociApp.RateGame.importGames.IGDBGameDataImporter;
import com.KociApp.RateGame.importGames.TokenGetter;
import com.KociApp.RateGame.registration.token.VeryficationToken;
import com.KociApp.RateGame.user.User;
import com.KociApp.RateGame.user.UserManagement.UserBan;
import com.KociApp.RateGame.user.UserManagement.UserBanRepository;
import com.KociApp.RateGame.user.UserService;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Component
public class ScheduledTasks {

    private final UserService userService;
    private final TokenGetter tokenGetter;
    private final IGDBGameDataImporter gameDataImporter;
    private final UserBanRepository userBanRepository;

    @Scheduled(fixedRate = 3600000) // Runs every 1 hour|| deletes expired tokens
    public void deleteExpiredTokens() {

        Calendar calendar = Calendar.getInstance();

        List<VeryficationToken> tokens = userService.getTokens();
        int counter = 0;

        for(VeryficationToken token : tokens){
            if(token.getExpirationTime().getTime() < calendar.getTime().getTime()){
                userService.deleteToken(token);
                counter++;
            }
        }
        log.info("Deleted " + counter + " expired tokens");
    }

    @Scheduled(fixedRate = 3600000) // Runs every 1 hour || deletes users that didn't confirm their account in time
    public void deleteNotConfirmedUsers() {

        List<User> users = userService.getUsers();
        int counter = 0;

        for(User user : users){
            //delete only if the token is not present, meaning it was expired and deleted, and the user is not banned, because a banned user would also be
            //set to not enabled and without a token
            if(!user.isEnabled() && userService.getTokenByUserId(user.getId()) == null && userBanRepository.findByUser(user).isEmpty()){
                userService.deleteUserById(user.getId());
                counter++;
            }
        }
        log.info("Deleted " + counter + " not confirmed in time users");
    }

    @Scheduled(fixedRate = 3600000) // Runs every 1 hour || imports new games from web
    public void importNewGames() throws UnirestException, IOException {

        Map<String, String> env = System.getenv();

        String accesToken = tokenGetter.getAccesToken();
        //import all genres
        gameDataImporter.importGenresFromIGDB(accesToken);
        //import all game platforms
        gameDataImporter.importPlatformsFromIGDB(accesToken);

        //import all new games to the database in batches until the response is 0 meaning we got everything up to date
        int gameCounter = 0;
        int gamesImported;
        do {
            gamesImported= gameDataImporter.importGamesFromIGDB(accesToken);
            gameCounter += gamesImported;
            try {
                // Sleep for 500 milliseconds (0.5 seconds)
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // Handle interrupted exception
                e.printStackTrace();
            }
        } while (gamesImported > 0 && !env.get("LIMIT_GAME_IMPORT_TESTING").equals("TRUE"));

        //import all new covers to the database
        int coverCounter = 0;
        int coversImported;
        do{
            coversImported = gameDataImporter.importCoversFromIGDB(accesToken);
            coverCounter += coversImported;
            try {
                // Sleep for 500 milliseconds (0.5 seconds)
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // Handle interrupted exception
                e.printStackTrace();
            }
        } while (coversImported > 0 && !env.get("LIMIT_GAME_IMPORT_TESTING").equals("TRUE"));

        log.info("Added " + gameCounter + " new games to the databe!");
        log.info("Added " + coverCounter + " new covers to the databe!");
    }

    @Scheduled(fixedRate = 3600000) // Runs every 1 hour|| unbans users after their penalty
    public void unbanUsers() {

        int counter = 0;
        List<UserBan> bans = userBanRepository.findAll();
        Date currDate = new Date();

        for(UserBan ban : bans){
            if(ban.getDuration().getTime() < currDate.getTime()){
                userService.unBanUserById(ban.getUser().getId());
                counter++;
                userBanRepository.delete(ban);
            }
        }
        log.info("Unbanned " + counter + " User!");
    }
}
