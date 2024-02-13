package com.KociApp.RateGame.user;

import com.KociApp.RateGame.registration.RegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements IUserService{

    private final UserRepository repository;
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
