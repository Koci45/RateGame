package com.KociApp.RateGame.user;
import com.KociApp.RateGame.registration.RegistrationRequest;
import com.KociApp.RateGame.registration.token.VeryficationToken;
import com.KociApp.RateGame.user.UserManagement.UserBan;

import java.util.List;

public interface IUserService {

    List<User> getUsers();
    User findById(Long id);
    User userRegistration(RegistrationRequest request);
    User findByEmail(String email);

    void saveUserToken(User user, String Token);

    String validateToken(String token);

    String deleteUserById(Long id);

    void deleteToken(VeryficationToken token);

    List<VeryficationToken> getTokens();

    VeryficationToken getTokenByUserId(Long userId);

    UserBan banUserById(Long userId, int duration);

    void unBanUserById(Long id);

}
