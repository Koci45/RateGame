package com.KociApp.RateGame.user;

import com.KociApp.RateGame.registration.RegistrationRequest;
import com.KociApp.RateGame.registration.token.VeryficationToken;
import com.KociApp.RateGame.registration.token.VeryficationTokenRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public User findById(Long id) {
        Optional<User> user = repository.findById(id);

        if(user.isEmpty()){
            throw new EntityNotFoundException("user with id: " + id.toString() + " not found");
        }

        return user.get();
    }

    @Override
    public User userRegistration(RegistrationRequest request) {
        Optional<User> user = repository.findByEmail(request.email());

        if(user.isPresent()){
            throw new EntityExistsException("User with email - " + request.email() + "already exists");
        }

        var newUser = new User();
        newUser.setUsername(request.username());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setRole("USER");
        newUser.setEmail(request.email());
        return repository.save(newUser);
    }

    @Override
    public User findByEmail(String email) {
        Optional<User> user = repository.findByEmail(email);

        if(user.isEmpty()){
            throw new EntityNotFoundException("user with email: " + email + " not found");
        }

        return user.get();
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
            return "Token expired ):";
        }

        User user = Thetoken.getUser();
        user.setEnabled(true);
        repository.save(user);

        return "valid";
    }

    @Override
    @Transactional
    public String deleteUserById(Long id) {

        tokenRepository.deleteByUserId(id);
        repository.delete(findById(id));

        return "User with id" + id + " has been removed";
    }

    @Override
    public void deleteToken(VeryficationToken token) {
        tokenRepository.delete(token);
    }

    @Override
    public List<VeryficationToken> getTokens() {
        return tokenRepository.findAll();
    }

    @Override
    public VeryficationToken getTokenByUserId(Long userId) {
        return tokenRepository.findByUserId(userId);
    }

}
