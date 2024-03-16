package com.KociApp.RateGame.user.UserInfo;

import com.KociApp.RateGame.user.User;
import com.KociApp.RateGame.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
public class DefaultLoggedInUserProvider implements LoggedInUserProvider {

    private final UserService userService;

    @Override
    public User getLoggedUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return userService.findByEmail(authentication.getName());
    }
}
