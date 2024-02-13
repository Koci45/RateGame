package com.KociApp.RateGame.event;

import com.KociApp.RateGame.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {

        User user = event.getUser();
        String verificationToken = UUID.randomUUID().toString();
        String url = event.getApplicationURL() + "/register/verifyEmail?token=" + verificationToken;
    }
}
