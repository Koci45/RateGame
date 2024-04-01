package com.KociApp.RateGame.api.controllers;

import com.KociApp.RateGame.registration.RegistrationController;
import com.KociApp.RateGame.registration.RegistrationRequest;
import com.KociApp.RateGame.registration.token.VeryficationToken;
import com.KociApp.RateGame.registration.token.VeryficationTokenRepository;
import com.KociApp.RateGame.user.User;
import com.KociApp.RateGame.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RegistrationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class RegistrationControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @MockBean
    VeryficationTokenRepository veryficationTokenRepository;

    @Test
    public void registerUserTest() throws Exception {
        RegistrationRequest request = new RegistrationRequest("test", "test@test", "123456");
        User user = new User(1L, "test", "test@test", "123456", "USER", false);

        when(userService.userRegistration(any())).thenReturn(user);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User " + user.getUsername() + " successfully registered, click on the link in the email we sent you!"));

        verify(userService, times(1)).userRegistration(request);
    }

    @Test
    public void verifyEmailWithNotVerifiedUserAndValidTokenTest() throws Exception {

        String token = "testToken";
        User user = new User(1L, "test", "test@test", "123456", "USER", false);
        VeryficationToken veryficationToken = new VeryficationToken(token, user);

        when(veryficationTokenRepository.findByToken(token)).thenReturn(veryficationToken);
        when(userService.validateToken(token)).thenReturn("valid");

        mockMvc.perform(get("/register/verifyEmail")
                        .param("token", token))
                .andExpect(status().isOk())
                .andExpect(content().string("Your account has been activated (="));

        verify(userService, times(1)).validateToken(token);
    }

    @Test
    public void verifyEmailWithNotVerifiedUserAndNotValidTokenTest() throws Exception {

        String token = "testToken";
        User user = new User(1L, "test", "test@test", "123456", "USER", false);
        VeryficationToken veryficationToken = new VeryficationToken(token, user);

        when(veryficationTokenRepository.findByToken(token)).thenReturn(veryficationToken);
        when(userService.validateToken(token)).thenReturn("Notvalid");

        mockMvc.perform(get("/register/verifyEmail")
                        .param("token", token))
                .andExpect(status().isOk())
                .andExpect(content().string("Something went wrong, your account has not been activated );"));

        verify(userService, times(1)).validateToken(token);
    }

    @Test
    public void verifyEmailWithAlreadyVerifiedUserTest() throws Exception {

        String token = "testToken";
        User user = new User(1L, "test", "test@test", "123456", "USER", true);
        VeryficationToken veryficationToken = new VeryficationToken(token, user);

        when(veryficationTokenRepository.findByToken(token)).thenReturn(veryficationToken);

        mockMvc.perform(get("/register/verifyEmail")
                        .param("token", token))
                .andExpect(status().isOk())
                .andExpect(content().string("Your email has been already verified"));

    }

}
