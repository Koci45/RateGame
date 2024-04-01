package com.KociApp.RateGame.api.controllers;

import com.KociApp.RateGame.game.Game;
import com.KociApp.RateGame.game.GameController;
import com.KociApp.RateGame.game.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GameController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class GameControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private GameService gameService;

    @Test
    public void createGameTest() throws Exception {

        Game game = new Game(1, "test", 1, new Date(), new Date(), "test", "test");

        when(gameService.save(any())).thenReturn(game);

        mockMvc.perform(post("/games").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(game)))
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(game)));
    }

    @Test
    public void getGamesTest() throws Exception {

        List<Game> games = new ArrayList<>();
        games.add(new Game(1, "test", 1, new Date(), new Date(), "test", "test"));
        games.add(new Game(2, "test", 1, new Date(), new Date(), "test", "test"));

        when(gameService.getGames()).thenReturn(games);

        mockMvc.perform(get("/games"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(games)));
    }

    @Test
    public void getGameByIdTest() throws Exception {

        Game game = new Game(1, "test", 1, new Date(), new Date(), "test", "test");

        when(gameService.findById(1)).thenReturn(game);

        mockMvc.perform(get("/games/byId/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(game)));
    }

    @Test
    public void getGamesByTitleLikeTest() throws Exception {

        List<Game> games = new ArrayList<>();
        games.add(new Game(1, "test1", 1, new Date(), new Date(), "test", "test"));
        games.add(new Game(2, "test2", 1, new Date(), new Date(), "test", "test"));

        when(gameService.findAllByTitleLike("test")).thenReturn(games);

        mockMvc.perform(get("/games/byKeyWord/{keyWord}", "test"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(games)));
    }
}
