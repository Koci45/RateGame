package com.KociApp.RateGame.user;

import com.KociApp.RateGame.exception.UserAlreadyExistsException;
import com.KociApp.RateGame.registration.RegistrationRequest;
import com.KociApp.RateGame.registration.token.VeryficationToken;
import com.KociApp.RateGame.registration.token.VeryficationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService implements IUserService{

    private final UserRepository repository;
    private final VeryficationTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public List<User> getUsers() {
        return repository.findAll();
    }

    @Override
    public User userRegistration(RegistrationRequest request) {

        Optional<User> user = repository.findByEmail(request.email());
        if(user.isPresent()){
            throw new UserAlreadyExistsException("User with email - " + request.email() + "already exists");
        }

        var newUser = new User();
        newUser.setUsername(request.username());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setRole(request.role());//usunac z requesta role, automatyznie ustwaic na "USER"
        newUser.setEmail(request.email());
        return repository.save(newUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public void saveUserToken(User user, String token) {

        var veryficationToken = new VeryficationToken(token, user);
        tokenRepository.save(veryficationToken);

    }

    @Override
    public String validateToken(String token) {

        VeryficationToken Thetoken = tokenRepository.findByToken(token);

        if(Thetoken == null){
            return "Invalid token -_-";
        }

        Calendar calendar = Calendar.getInstance();

        if(Thetoken.getExpirationTime().getTime() < calendar.getTime().getTime()){
            tokenRepository.delete(Thetoken);
            return "Token expired ):";
        }

        User user = Thetoken.getUser();
        user.setEnabled(true);
        repository.save(user);

        return "valid";
    }
}
