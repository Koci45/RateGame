package com.KociApp.RateGame.registration;

import com.KociApp.RateGame.event.RegistrationCompleteEvent;
import com.KociApp.RateGame.registration.token.VeryficationToken;
import com.KociApp.RateGame.registration.token.VeryficationTokenRepository;
import com.KociApp.RateGame.user.User;
import com.KociApp.RateGame.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final VeryficationTokenRepository tokenRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String registerUser(@RequestBody RegistrationRequest request, final HttpServletRequest httpRequest){

        User user = userService.userRegistration(request);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationURL(httpRequest)));

        return "User " + user.getUsername() + " successfully registered, click on the link in the email we sent you!";
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token){
        VeryficationToken Thetoken = tokenRepository.findByToken(token);

        if(Thetoken.getUser().isEnabled()){
            return "Your email has been already verified";
        }

        String verificationResult = userService.validateToken(token);

        if(verificationResult.equals("valid")){
            return "Your account has been activated (=";
        }
        return "Something went wrong, your account has not been activated );";
    }

    private String applicationURL(HttpServletRequest httpRequest) {

        return "http://" + httpRequest.getServerName() + ":" + httpRequest.getServerPort() + httpRequest.getContextPath();
    }
}
