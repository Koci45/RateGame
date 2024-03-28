package com.KociApp.RateGame.api.services;

import com.KociApp.RateGame.registration.RegistrationRequest;
import com.KociApp.RateGame.registration.token.VeryficationToken;
import com.KociApp.RateGame.registration.token.VeryficationTokenRepository;
import com.KociApp.RateGame.review.ReviewRepository;
import com.KociApp.RateGame.review.reports.ReviewReportsRepository;
import com.KociApp.RateGame.user.User;
import com.KociApp.RateGame.user.UserManagement.UserBan;
import com.KociApp.RateGame.user.UserManagement.UserBanRepository;
import com.KociApp.RateGame.user.UserRepository;
import com.KociApp.RateGame.user.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private VeryficationTokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserBanRepository userBanRepository;

    @Mock
    private ReviewReportsRepository reviewReportsRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void getUsersReturnsAllUsersTest(){

        List<User> users = new ArrayList<>();
        users.add(new User(1L, "tset1", "test@test1", "123456", "USER", true));
        users.add(new User(2L, "tset2", "test@test2", "123456", "USER", false));
        users.add(new User(3L, "tset3", "test@test3", "123456", "USER", true));

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getUsers();

        Assertions.assertThat(result.size()).isEqualTo(3L);
        Assertions.assertThat(result.get(0).getEmail()).isEqualTo("test@test1");
        Assertions.assertThat(result.get(1).getEmail()).isEqualTo("test@test2");
        Assertions.assertThat(result.get(2).getEmail()).isEqualTo("test@test3");
    }

    @Test
    public void findByIdReturnsByIdWhenIdExistsTest(){

        User userOne = new User(1L, "tset1", "test@test1", "123456", "USER", true);
        User userTwo = new User(2L, "tset2", "test@test2", "123456", "USER", false);

        when(userRepository.findById(2L)).thenReturn(Optional.of(userTwo));

        User result = userService.findById(2L);

        Assertions.assertThat(result.getId()).isEqualTo(2L);
        Assertions.assertThat(result.getEmail()).isEqualTo("test@test2");
    }

    @Test
    public void findByIdThrowsWhenIdNotExistsTest(){

        when(userRepository.findById(3L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> userService.findById(3L)).isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("user with id: 3 not found");
    }

    @Test
    public void userRegistrationSuccesTest(){

        RegistrationRequest registrationRequest = new RegistrationRequest("test1", "register@test", "123456");
        User registeredUser = new User(1L,"test","register@test","encodedPassword","USER", false);

        when(userRepository.findByEmail("register@test")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenReturn(registeredUser);

        User result = userService.userRegistration(registrationRequest);

        verify(userRepository, times(1)).save(any(User.class));

        Assertions.assertThat(result.getEmail()).isEqualTo("register@test");
        Assertions.assertThat(result.getUsername()).isEqualTo("test");
        Assertions.assertThat(result.getPassword()).isEqualTo("encodedPassword");
        Assertions.assertThat(result.getRole()).isEqualTo("USER");
        Assertions.assertThat(result.isEnabled()).isFalse();
    }

    @Test
    public void userRegistrationWhenUserWithThatEmailExistsTest(){

        RegistrationRequest registrationRequest = new RegistrationRequest("test1", "register@test", "123456");
        User registeredUser = new User(1L,"test","register@test","encodedPassword","USER", false);

        when(userRepository.findByEmail("register@test")).thenReturn(Optional.of(registeredUser));

        Assertions.assertThatThrownBy(() -> userService.userRegistration(registrationRequest)).isInstanceOf(EntityExistsException.class)
                .hasMessageContaining("User with email - register@test already exists");
    }

    @Test
    public void findByEmailReturnsByEmailWhenEmailExistsTest(){

        User userOne = new User(1L, "tset1", "test@test1", "123456", "USER", true);
        User userTwo = new User(2L, "tset2", "test@test2", "123456", "USER", false);

        when(userRepository.findByEmail("test@test1")).thenReturn(Optional.of(userOne));

        User result = userService.findByEmail("test@test1");

        Assertions.assertThat(result.getId()).isEqualTo(1L);
        Assertions.assertThat(result.getEmail()).isEqualTo("test@test1");
    }

    @Test
    public void findByEmailThrowsWhenEmailNotExistsTest(){

        when(userRepository.findByEmail("test@test3")).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> userService.findByEmail("test@test3")).isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("user with email: test@test3 not found");
    }

    @Test
    public void validateTokenWithValidToken(){

        String tokenBody = "validToken123";
        VeryficationToken token = new VeryficationToken("validToken123", new User());

        when(tokenRepository.findByToken("validToken123")).thenReturn(token);

        when(userRepository.save(any())).thenReturn(new User());

        String result = userService.validateToken(tokenBody);

        Assertions.assertThat(result).isEqualTo("valid");
    }

    @Test
    public void validateTokenWithInvalidToken(){

        String tokenBody = "invalidToken123";
        VeryficationToken token = new VeryficationToken("invalidToken123", new User());

        when(tokenRepository.findByToken("invalidToken123")).thenReturn(null);

        String result = userService.validateToken(tokenBody);

        Assertions.assertThat(result).isEqualTo("Invalid token -_-");
    }

    @Test
    public void validateTokenWithExpiredToken(){

        String tokenBody = "expiredToken123";
        VeryficationToken token = new VeryficationToken("expiredToken123", new User());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date expiredDate = calendar.getTime();

        token.setExpirationTime(expiredDate);

        when(tokenRepository.findByToken("expiredToken123")).thenReturn(token);

        String result = userService.validateToken(tokenBody);

        Assertions.assertThat(result).isEqualTo("Token expired ):");
    }

    @Test
    public void deleteUserByIdWithExistingIdTest(){

        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        String result = userService.deleteUserById(1L);

        verify(tokenRepository, times(1)).deleteByUserId(1L);
        verify(userBanRepository, times(1)).deleteAllByUser_Id(1L);
        verify(reviewReportsRepository, times(1)).deleteAllByUser_Id(1L);
        verify(reviewRepository, times(1)).deleteAllByUser_Id(1L);
        verify(userRepository, times(1)).delete(user);

        Assertions.assertThat(result).isEqualTo("User with id 1 has been removed");
    }

    @Test
    public void deleteUserByIdWithNotExistingIdTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> userService.deleteUserById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("not found");

    }

    @Test
    public void deleteTokenTest(){

        VeryficationToken token = new VeryficationToken();

        userService.deleteToken(token);

        verify(tokenRepository, times(1)).delete(token);
    }

    @Test
    public void getTokensTest(){

        List<VeryficationToken> tokens = new ArrayList<>();
        tokens.add(new VeryficationToken("token1", new User()));
        tokens.add(new VeryficationToken("token2", new User()));
        tokens.add(new VeryficationToken("token2", new User()));

        when(userService.getTokens()).thenReturn(tokens);

        List<VeryficationToken> result = userService.getTokens();

        Assertions.assertThat(result.size()).isEqualTo(3);
        Assertions.assertThat(result.get(1).getToken()).isEqualTo("token2");
    }

    @Test
    public void getTokenByUserIdTest(){

        VeryficationToken expectedToken = new VeryficationToken("token", new User());

        when(tokenRepository.findByUserId(1L)).thenReturn(expectedToken);

        VeryficationToken actualToken = userService.getTokenByUserId(1L);

        verify(tokenRepository, times(1)).findByUserId(1L);

        // Verify that the returned token matches the expected token
        Assertions.assertThat(expectedToken).isEqualTo(actualToken);
    }

    @Test
    public void testBanUserByIdWithExistingId_NewBan() {

        User user = new User(1L,"test","register@test","encodedPassword","USER", true);
        int duration = 1; // one day

        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_YEAR, duration);
        Date tomorrow = calendar.getTime();

        UserBan newUserBan = new UserBan(1L, tomorrow, user);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userBanRepository.save(any())).thenReturn(newUserBan);



        UserBan result = userService.banUserById(1L, duration);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        Assertions.assertThat(userCaptor.getValue().isEnabled()).isFalse();
        Assertions.assertThat(userCaptor.getValue().getId()).isEqualTo(1L);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getUser().getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(result.getDuration()).isEqualTo(tomorrow);
    }

    @Test
    public void testBanUserByIdWithExistingId_AddingOnExistingBan() {

        //this tests test the functionality that adds penalty to existing ban,
        //here there is already a ban present with duration for one day,
        //then we add new ban for one day, so the metho should add the penalty up
        //and we should get ban that expires in 2 days

        User user = new User(1L,"test","register@test","encodedPassword","USER", true);
        int duration = 1; // one day

        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_YEAR, duration);
        Date tomorrow = calendar.getTime();

        UserBan newUserBan = new UserBan(1L, tomorrow, user);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userBanRepository.save(any())).thenReturn(newUserBan);
        when(userBanRepository.findByUser(any())).thenReturn(Optional.of(newUserBan));



        UserBan result = userService.banUserById(1L, duration);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        calendar.add(Calendar.DAY_OF_YEAR, duration);//adding one day since we added up the penalties

        Assertions.assertThat(userCaptor.getValue().isEnabled()).isFalse();
        Assertions.assertThat(userCaptor.getValue().getId()).isEqualTo(1L);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getUser().getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(result.getDuration()).isEqualTo(calendar.getTime());
    }

    @Test
    public void unBanUserTest(){
        User bannedUser = new User(1L,"test","register@test","encodedPassword","USER", false);

        when(userRepository.findById(any())).thenReturn(Optional.of(bannedUser));

        userService.unBanUserById(1L);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        Assertions.assertThat(userCaptor.getValue().isEnabled()).isTrue();
        Assertions.assertThat(userCaptor.getValue().getEmail()).isEqualTo(bannedUser.getEmail());
    }

}
