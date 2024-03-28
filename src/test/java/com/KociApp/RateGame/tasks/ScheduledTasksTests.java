package com.KociApp.RateGame.tasks;

import com.KociApp.RateGame.registration.token.VeryficationToken;
import com.KociApp.RateGame.user.User;
import com.KociApp.RateGame.user.UserManagement.UserBan;
import com.KociApp.RateGame.user.UserManagement.UserBanRepository;
import com.KociApp.RateGame.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScheduledTasksTests {

    @Mock
    private UserService userService;

    @Mock
    private UserBanRepository userBanRepository;

    @InjectMocks
    private ScheduledTasks scheduledTasks;

    @Test
    public void deleteExpiredTokensTest(){

        List<VeryficationToken> tokens = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date expiredDate = calendar.getTime();

        VeryficationToken[] tokenArray = new VeryficationToken[2];
        tokenArray[0] = new VeryficationToken();
        tokenArray[1] = new VeryficationToken();
        tokenArray[0].setExpirationTime(expiredDate);
        tokenArray[1].setExpirationTime(expiredDate);

        tokens.add(tokenArray[0]);
        tokens.add(tokenArray[1]);

        when(userService.getTokens()).thenReturn(tokens);

        scheduledTasks.deleteExpiredTokens();

        verify(userService, times(2)).deleteToken(any());
    }

    @Test
    public void notDeleteNotExpiredTokensTest(){

        List<VeryficationToken> tokens = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, 2);
        Date validDate = calendar.getTime();

        VeryficationToken[] tokenArray = new VeryficationToken[3];
        tokenArray[0] = new VeryficationToken();
        tokenArray[1] = new VeryficationToken();
        tokenArray[2] = new VeryficationToken();
        tokenArray[0].setExpirationTime(validDate);
        tokenArray[1].setExpirationTime(validDate);
        tokenArray[2].setExpirationTime(validDate);

        tokens.add(tokenArray[0]);
        tokens.add(tokenArray[1]);
        tokens.add(tokenArray[2]);

        when(userService.getTokens()).thenReturn(tokens);

        scheduledTasks.deleteExpiredTokens();

        verify(userService, times(0)).deleteToken(any());
    }

    @Test
    public void deleteNotConfirmedAndNotBannedAndWithoutTokenUserTest(){

        User notConfirmedInTimeUser = new User();

        //user with no token, and no ban -- delete
        notConfirmedInTimeUser.setEnabled(false);
        notConfirmedInTimeUser.setId(1L);

        List<User> users = new ArrayList<>();
        users.add(notConfirmedInTimeUser);

        when(userService.getUsers()).thenReturn(users);

        //notConfirmedInTimeUser
        when(userService.getTokenByUserId(1L)).thenReturn(null);
        when(userBanRepository.findByUser(notConfirmedInTimeUser)).thenReturn(Optional.empty());

        scheduledTasks.deleteNotConfirmedUsers();

        verify(userService, times(1)).deleteUserById(any());
    }

    @Test
    public void notDeleteNotConfirmedNotBannedAndWithTokenUserTest(){

        User notConfirmedUserWithToken = new User();

        //not confirmed user with token and no ban -- don't delete
        notConfirmedUserWithToken.setEnabled(false);
        notConfirmedUserWithToken.setId(2L);

        List<User> users = new ArrayList<>();

        users.add(notConfirmedUserWithToken);

        when(userService.getUsers()).thenReturn(users);

        //not confirmed user with token and no ban
        when(userService.getTokenByUserId(2L)).thenReturn(new VeryficationToken("tokenseconduser", notConfirmedUserWithToken));

        scheduledTasks.deleteNotConfirmedUsers();

        verify(userService, times(0)).deleteUserById(any());
    }

    @Test
    public void notDeleteConfirmedAndBannedUserTest(){

        User confirmedAndBannedUser = new User();

        //user with ban -- don't delete
        confirmedAndBannedUser.setEnabled(false);
        confirmedAndBannedUser.setId(3L);

        List<User> users = new ArrayList<>();

        users.add(confirmedAndBannedUser);

        when(userService.getUsers()).thenReturn(users);

        //user with ban
        when(userService.getTokenByUserId(3L)).thenReturn(null);
        when(userBanRepository.findByUser(confirmedAndBannedUser)).thenReturn(Optional.of(new UserBan(1L, new Date(), confirmedAndBannedUser)));

        scheduledTasks.deleteNotConfirmedUsers();

        verify(userService, times(0)).deleteUserById(any());
    }

    @Test
    public void notDeleteConfirmedUserTest(){

        User confirmedUser = new User();

        //confirmed user -- don't delete
        confirmedUser.setEnabled(true);
        confirmedUser.setId(4L);

        List<User> users = new ArrayList<>();

        users.add(confirmedUser);

        when(userService.getUsers()).thenReturn(users);

        scheduledTasks.deleteNotConfirmedUsers();

        verify(userService, times(0)).deleteUserById(any());
    }

    @Test
    public void unbanUsersWithExpiredBanTest(){

        User bannedAfterBanExpirationUser = new User();

        UserBan expiredBan = new UserBan();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date expiredDate = calendar.getTime();

        expiredBan.setId(2L);
        expiredBan.setUser(bannedAfterBanExpirationUser);
        expiredBan.setDuration(expiredDate);

        List<UserBan> userBans = new ArrayList<>();
        userBans.add(expiredBan);

        when(userBanRepository.findAll()).thenReturn(userBans);

        scheduledTasks.unbanUsers();

        verify(userService, times(1)).unBanUserById(any());
        verify(userBanRepository, times(1)).delete(any());
    }

    @Test
    public void notUnbanUsersWithNotExpiredBanTest(){

        User bannedUser = new User();

        UserBan notExpiredBan = new UserBan();

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, 2);
        Date validDate = calendar.getTime();

        notExpiredBan.setId(1L);
        notExpiredBan.setUser(bannedUser);
        notExpiredBan.setDuration(validDate);

        List<UserBan> userBans = new ArrayList<>();
        userBans.add(notExpiredBan);

        when(userBanRepository.findAll()).thenReturn(userBans);

        scheduledTasks.unbanUsers();

        verify(userService, times(0)).unBanUserById(any());
        verify(userBanRepository, times(0)).delete(any());
    }

}
