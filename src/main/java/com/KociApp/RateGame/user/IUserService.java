package com.KociApp.RateGame.user;
import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<User> getUsers();

    User userRegistration(RegistrationRequest request);
    Optional<User> findByEmail(String email);
}
