package com.KociApp.RateGame.registration;

import com.KociApp.RateGame.user.User;
import com.KociApp.RateGame.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @PostMapping
    public String registerUser(@RequestBody RegistrationRequest request){

        User user = userService.userRegistration(request);

        return "User " + user.getUsername() + " succesfully registered, click on the link in the email we sent you!";
    }
}
