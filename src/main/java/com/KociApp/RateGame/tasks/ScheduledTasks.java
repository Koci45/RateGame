package com.KociApp.RateGame.tasks;

import com.KociApp.RateGame.registration.token.VeryficationToken;
import com.KociApp.RateGame.user.User;
import com.KociApp.RateGame.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class ScheduledTasks {

    private final UserService userService;

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

    @Scheduled(fixedRate = 3600000) // Runs every 1 hour || deletes users that didnt confirm their acciount in time
    public void deleteNotConfirmedUsers() {

        List<User> users = userService.getUsers();
        int counter = 0;

        for(User user : users){
            if(!user.isEnabled() && userService.getTokenByUserId(user.getId()) == null){
                userService.deleteUserById(user.getId());
                counter++;
            }
        }
        log.info("Deleted " + counter + " not confirmed in time users");
    }
}
