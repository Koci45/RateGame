package com.KociApp.RateGame.api.controllers;

import com.KociApp.RateGame.game.Game;
import com.KociApp.RateGame.game.cover.Cover;
import com.KociApp.RateGame.game.cover.CoverController;
import com.KociApp.RateGame.game.cover.CoverService;
import com.KociApp.RateGame.review.*;
import com.KociApp.RateGame.review.helperClasses.AverageGameRatingCalculatorProvider;
import com.KociApp.RateGame.review.helperClasses.ReviewToReviewResponseTranslatorProvider;
import com.KociApp.RateGame.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CoverController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CoverControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private CoverService coverService;

    @Test
    public void getCoverByGameIdTest() throws Exception {

        Cover cover = new Cover(1, 1, "test");

        when(coverService.findCoverByGameId(1)).thenReturn(cover);

        mockMvc.perform(get("/covers/{gameId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(cover)));
    }
}
