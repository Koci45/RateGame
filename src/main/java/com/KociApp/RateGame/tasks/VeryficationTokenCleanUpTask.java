package com.KociApp.RateGame.tasks;

import com.KociApp.RateGame.registration.token.VeryficationToken;
import com.KociApp.RateGame.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

@RequiredArgsConstructor
@Component
public class VeryficationTokenCleanUpTask {

    private final UserService userService;

    @Scheduled(fixedRate = 6000) // Runs every 10s minutes
    public void deleteExpiredTokens() {

        Calendar calendar = Calendar.getInstance();

        List<VeryficationToken> tokens = userService.getTokens();


        for(VeryficationToken token : tokens){
            System.out.println("Token : " + token.getExpirationTime());
            if(token.getExpirationTime().getTime() < calendar.getTime().getTime()){
                System.out.println("after");
                userService.deleteToken(token);
            }
        }

    }
}
