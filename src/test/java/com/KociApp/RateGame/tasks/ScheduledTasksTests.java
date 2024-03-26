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

        calendar.add(Calendar.DAY_OF_YEAR, 2);
        Date validDate = calendar.getTime();

        VeryficationToken[] tokenArray = new VeryficationToken[3];
        tokenArray[0] = new VeryficationToken();
        tokenArray[1] = new VeryficationToken();
        tokenArray[2] = new VeryficationToken();
        tokenArray[0].setExpirationTime(expiredDate);
        tokenArray[1].setExpirationTime(expiredDate);
        tokenArray[2].setExpirationTime(validDate);

        tokens.add(tokenArray[0]);
        tokens.add(tokenArray[1]);
        tokens.add(tokenArray[2]);

        when(userService.getTokens()).thenReturn(tokens);

        scheduledTasks.deleteExpiredTokens();

        verify(userService, times(2)).deleteToken(any());
    }

    @Test
    public void deleteNotConfirmedUserTest(){

        User notConfirmedInTimeUser = new User();
        User notConfirmedUserWithToken = new User();
        User confirmedAndBannedUser = new User();
        User confirmedUser = new User();

        //user with no token, and no ban -- delete
        notConfirmedInTimeUser.setEnabled(false);
        notConfirmedInTimeUser.setId(1L);

        //not confirmed user with token and no ban -- don't delete
        notConfirmedUserWithToken.setEnabled(false);
        notConfirmedUserWithToken.setId(2L);

        //user with ban -- don't delete
        confirmedAndBannedUser.setEnabled(false);
        confirmedAndBannedUser.setId(3L);

        //confirmed user -- don't delete
        confirmedUser.setEnabled(true);
        confirmedUser.setId(4L);

        List<User> users = new ArrayList<>();
        users.add(notConfirmedInTimeUser);
        users.add(notConfirmedUserWithToken);
        users.add(confirmedAndBannedUser);

        when(userService.getUsers()).thenReturn(users);

        //notConfirmedInTimeUser
        when(userService.getTokenByUserId(1L)).thenReturn(null);
        when(userBanRepository.findByUser(notConfirmedInTimeUser)).thenReturn(Optional.empty());

        //not confirmed user with token and no ban
        when(userService.getTokenByUserId(2L)).thenReturn(new VeryficationToken("tokenseconduser", notConfirmedUserWithToken));

        //user with ban
        when(userService.getTokenByUserId(3L)).thenReturn(null);
        when(userBanRepository.findByUser(confirmedAndBannedUser)).thenReturn(Optional.of(new UserBan(1L, new Date(), confirmedAndBannedUser)));

        scheduledTasks.deleteNotConfirmedUsers();

        verify(userService, times(1)).deleteUserById(any());
    }

    @Test
    public void unbanUsersTest(){

        User bannedUser = new User();
        User bannedAfterBanExpirationUser = new User();

        UserBan notExpiredBan = new UserBan();
        UserBan expiredBan = new UserBan();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date expiredDate = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 2);
        Date validDate = calendar.getTime();

        notExpiredBan.setId(1L);
        notExpiredBan.setUser(bannedUser);
        notExpiredBan.setDuration(validDate);

        expiredBan.setId(2L);
        expiredBan.setUser(bannedAfterBanExpirationUser);
        expiredBan.setDuration(expiredDate);

        List<UserBan> userBans = new ArrayList<>();
        userBans.add(notExpiredBan);
        userBans.add(expiredBan);

        when(userBanRepository.findAll()).thenReturn(userBans);

        scheduledTasks.unbanUsers();

        verify(userService, times(1)).unBanUserById(any());
        verify(userBanRepository, times(1)).delete(any());
    }

}
