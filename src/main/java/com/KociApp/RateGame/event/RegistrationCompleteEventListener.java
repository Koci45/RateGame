package com.KociApp.RateGame.event;

import com.KociApp.RateGame.user.User;
import com.KociApp.RateGame.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;


@RequiredArgsConstructor
@Slf4j
@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final UserService userService;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {

        User user = event.getUser();
        //creating token
        String verificationToken = UUID.randomUUID().toString();
        //creating confirmation link
        String url = event.getApplicationURL() + "/register/verifyEmail?token=" + verificationToken;
        //saving the token
        userService.saveUserToken(user, verificationToken);

        log.info("click the link to verify your email: {}", url);
    }
}
