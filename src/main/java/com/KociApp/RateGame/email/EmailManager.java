package com.KociApp.RateGame.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@RequiredArgsConstructor
@Setter
@Getter
@Component
public class EmailManager {

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String url, String username, String userEmail) throws MessagingException, UnsupportedEncodingException {

        String topic = "Verify your RateGame account!";
        String sender = "RateGame";
        String content = "<h1> Hi, " + username +  " welcome to the RateGameCommunity! </h1>" +
                "<p> There is only one thing left to do before you can Rate your games.</p>" +
                "<p> Please click on the link below to verify your account </p>" +
                "<a href=\"" + url + "\">Here!!! </a>";

        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("rategamegame@gmail.com", sender);
        messageHelper.setTo(userEmail);
        messageHelper.setSubject(topic);
        messageHelper.setText(content, true);
        mailSender.send(message);
    }
}
