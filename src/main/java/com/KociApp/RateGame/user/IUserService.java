package com.KociApp.RateGame.user;
import com.KociApp.RateGame.registration.RegistrationRequest;
import com.KociApp.RateGame.registration.token.VeryficationToken;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<User> getUsers();

    User userRegistration(RegistrationRequest request);
    Optional<User> findByEmail(String email);

    void saveUserToken(User user, String Token);

    String validateToken(String token);

}
