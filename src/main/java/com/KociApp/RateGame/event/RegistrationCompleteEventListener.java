package com.KociApp.RateGame.event;

import com.KociApp.RateGame.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {

        User user = event.getUser();
        String verificationToken = UUID.randomUUID().toString();
        String url = event.getApplicationURL() + "/register/verifyEmail?token=" + verificationToken;

        log.info("click the link to verify your email: {}", url);
    }
}
