package com.KociApp.RateGame.event;

import com.KociApp.RateGame.email.EmailManager;
import com.KociApp.RateGame.user.User;
import com.KociApp.RateGame.user.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;


@RequiredArgsConstructor
@Slf4j
@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final UserService userService;
    private final EmailManager emailManager;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {

        User user = event.getUser();
        //creating token
        String verificationToken = UUID.randomUUID().toString();
        //creating confirmation link
        String url = event.getApplicationURL() + "/register/verifyEmail?token=" + verificationToken;
        //saving the token
        userService.saveUserToken(user, verificationToken);

        //printing the url for account verification
        log.info("click the link to verify your email: {}", url);

        try {
            emailManager.sendVerificationEmail(url, user.getUsername(), user.getEmail());
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
