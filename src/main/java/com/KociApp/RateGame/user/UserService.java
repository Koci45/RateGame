package com.KociApp.RateGame.user;

import com.KociApp.RateGame.registration.RegistrationRequest;

import java.util.List;
import java.util.Optional;

public class UserService implements IUserService{
    @Override
    public List<User> getUsers() {
        return null;
    }

    @Override
    public User userRegistration(RegistrationRequest request) {
        return null;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }
}
