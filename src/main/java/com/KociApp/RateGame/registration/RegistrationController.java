package com.KociApp.RateGame.registration;

import com.KociApp.RateGame.event.RegistrationCompleteEvent;
import com.KociApp.RateGame.user.User;
import com.KociApp.RateGame.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;
    private final ApplicationEventPublisher publisher;

    @PostMapping
    public String registerUser(RegistrationRequest request, final HttpServletRequest httpRequest){

        User user = userService.userRegistration(request);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationURL(httpRequest)));

        return "User " + user.getUsername() + " succesfully registered, click on the link in the email we sent you!";
    }

    private String applicationURL(HttpServletRequest httpRequest) {

        return "http://" + httpRequest.getServerName() + ":" + httpRequest.getServerPort() + httpRequest.getContextPath();
    }
}
