package com.KociApp.RateGame.api.controllers;

import com.KociApp.RateGame.registration.token.VeryficationToken;
import com.KociApp.RateGame.user.User;
import com.KociApp.RateGame.user.UserController;
import com.KociApp.RateGame.user.UserManagement.UserBan;
import com.KociApp.RateGame.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void getUsersTest() throws Exception {

        List<User> users = new ArrayList<>();
        users.add(new User(1L, "test1", "test@test", "123456", "USER", true));
        users.add(new User(2L, "test2", "test@test", "123456", "USER", true));

        when(userService.getUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(users)));
    }

    @Test
    public void getUserTest() throws Exception {

        User user = new User(1L, "test1", "test@test", "123456", "USER", true);

        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/users/byId/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(user)));
    }

    @Test
    public void deleteUserByIdTest() throws Exception {

        User user = new User(1L, "test1", "test@test", "123456", "USER", true);

        when(userService.deleteUserById(1L)).thenReturn(objectMapper.writeValueAsString(user));

        mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(user)));

        verify(userService, times(1)).deleteUserById(1L);
    }

    @Test
    public void getTokensTest() throws Exception {

        List<VeryficationToken> tokens = new ArrayList<>();
        tokens.add(new VeryficationToken("test1", new User()));
        tokens.add(new VeryficationToken("test2", new User()));

        when(userService.getTokens()).thenReturn(tokens);

        mockMvc.perform(get("/users/tokens"))
                .andExpect(status().isOk());
    }

    @Test
    public void getTokenByUserId() throws Exception {

        VeryficationToken token = new VeryficationToken("test1", new User());

        when(userService.getTokenByUserId(1L)).thenReturn(token);

        mockMvc.perform(get("/users/tokens/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void banUserByIdWithIdAndDurationTest() throws Exception {

        User user = new User(1L, "test1", "test@test", "123456", "USER", false);
        UserBan userBan = new UserBan(1L, new Date(), user);

        when(userService.banUserById(1L, 1)).thenReturn(userBan);

        mockMvc.perform(post("/users/ban").param("userId", "1").param("duration", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(userBan)));

        verify(userService, times(1)).banUserById(1L, 1);

    }

    @Test
    public void banUserByIdWithNoIdAndDurationTest() throws Exception {

        mockMvc.perform(post("/users/ban").param("duration", "1"))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void banUserByIdWithIdAndNoDurationTest() throws Exception {

        mockMvc.perform(post("/users/ban").param("userId", "1"))
                .andExpect(status().isBadRequest());

    }
}
