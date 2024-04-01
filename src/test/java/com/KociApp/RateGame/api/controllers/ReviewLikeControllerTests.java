package com.KociApp.RateGame.api.controllers;

import com.KociApp.RateGame.game.Game;
import com.KociApp.RateGame.review.*;
import com.KociApp.RateGame.review.helperClasses.AverageGameRatingCalculatorProvider;
import com.KociApp.RateGame.review.helperClasses.ReviewToReviewResponseTranslatorProvider;
import com.KociApp.RateGame.review.likes.ReviewLike;
import com.KociApp.RateGame.review.likes.ReviewLikeController;
import com.KociApp.RateGame.review.likes.ReviewLikeService;
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

@WebMvcTest(controllers = ReviewLikeController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ReviewLikeControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private ReviewLikeService reviewLikeService;

    @Test
    public void countLikesByReviewIdTest() throws Exception {

        when(reviewLikeService.countLikesByReviewId(1L)).thenReturn(5);

        mockMvc.perform(get("/reviewLikes/likes/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    public void countDislikesByReviewIdTest() throws Exception {

        when(reviewLikeService.countDislikesByReviewId(1L)).thenReturn(5);

        mockMvc.perform(get("/reviewLikes/dislikes/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    public void createLikeDislikeTest() throws Exception {

        ReviewLike reviewLike = new ReviewLike(1L, true, new User(), new Review());

        when(reviewLikeService.createLikeDislike(any())).thenReturn(reviewLike);

        mockMvc.perform(post("/reviewLikes").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(reviewLike)))
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(reviewLike)));
    }
}
