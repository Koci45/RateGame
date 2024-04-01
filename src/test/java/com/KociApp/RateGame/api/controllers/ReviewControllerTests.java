package com.KociApp.RateGame.api.controllers;

import com.KociApp.RateGame.game.Game;
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

@WebMvcTest(controllers = ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ReviewControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private ReviewToReviewResponseTranslatorProvider reviewToReviewResponseTranslatorProvider;

    @MockBean
    private AverageGameRatingCalculatorProvider averageGameRatingCalculatorProvider;

    @Test
    public void createReviewTestWithNotEmptyContentAbdGameId() throws Exception {

        ReviewRequest reviewRequest = new ReviewRequest("test", (byte) 50, 1);

        Review expectedReview = new Review(1L, "test",new Date(), new User(), new Game(), (byte) 50);

        when(reviewService.save(reviewRequest)).thenReturn(expectedReview);

        mockMvc.perform(post("/reviews").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(expectedReview)));

        verify(reviewService, times(1)).save(reviewRequest);
    }

    @Test
    public void createReviewTestWithEmptyContentAbdGameId() throws Exception {

        ReviewRequest reviewRequest = new ReviewRequest("", (byte) 50, 1);

        mockMvc.perform(post("/reviews").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void createReviewTestWithNotContentAndEmptyGameId() throws Exception {

        ReviewRequest reviewRequest = new ReviewRequest("test", (byte) 50, 0);

        mockMvc.perform(post("/reviews").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void getReviewsTest() throws Exception {

        User user = new User(1L, "test1", "test@test", "123456", "USER", true);

        List<Review> reviews = new ArrayList<>();
        Review reviewOne = new Review(1L, "test", new Date(), user, new Game(), (byte) 50);
        Review reviewTwo = new Review(2L, "test", new Date(), user, new Game(), (byte) 50);
        reviews.add(reviewOne);
        reviews.add(reviewTwo);

        List<ReviewResponse> reviewResponses = new ArrayList<>();
        reviewResponses.add(new ReviewResponse(reviewOne.getId(), reviewOne.getContent(), reviewOne.getCreationDate(), reviewOne.getGame(), reviewOne.getUser().getUsername(), reviewOne.getRating()));
        reviewResponses.add(new ReviewResponse(reviewTwo.getId(), reviewTwo.getContent(), reviewTwo.getCreationDate(), reviewTwo.getGame(), reviewTwo.getUser().getUsername(), reviewTwo.getRating()));

        when(reviewService.getReviews()).thenReturn(reviews);
        when(reviewToReviewResponseTranslatorProvider.translate(reviews)).thenReturn(reviewResponses);

        mockMvc.perform(get("/reviews"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(reviewResponses)));
    }

    @Test
    public void deleteReviewTest() throws Exception {

        when(reviewService.remove(1L)).thenReturn("Review -1 removed");

        mockMvc.perform(delete("/reviews/deleteById/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("Review -1 removed"));
    }

    @Test
    public void findByUserIdTest() throws Exception {

        User user = new User(1L, "test1", "test@test", "123456", "USER", true);

        List<Review> reviews = new ArrayList<>();
        Review reviewOne = new Review(1L, "test", new Date(), user, new Game(), (byte) 50);
        Review reviewTwo = new Review(2L, "test", new Date(), user, new Game(), (byte) 50);
        reviews.add(reviewOne);
        reviews.add(reviewTwo);

        List<ReviewResponse> reviewResponses = new ArrayList<>();
        reviewResponses.add(new ReviewResponse(reviewOne.getId(), reviewOne.getContent(), reviewOne.getCreationDate(), reviewOne.getGame(), reviewOne.getUser().getUsername(), reviewOne.getRating()));
        reviewResponses.add(new ReviewResponse(reviewTwo.getId(), reviewTwo.getContent(), reviewTwo.getCreationDate(), reviewTwo.getGame(), reviewTwo.getUser().getUsername(), reviewTwo.getRating()));

        when(reviewService.findByUserId(1L)).thenReturn(reviews);
        when(reviewToReviewResponseTranslatorProvider.translate(reviews)).thenReturn(reviewResponses);

        mockMvc.perform(get("/reviews/findByUserId/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(reviewResponses)));
    }

    @Test
    public void findByGameIdTest() throws Exception {

        Game game = new Game(1, "test", 1, new Date(), new Date(), "test", "test");
        User user = new User(1L, "test1", "test@test", "123456", "USER", true);

        List<Review> reviews = new ArrayList<>();
        Review reviewOne = new Review(1L, "test", new Date(), user, game, (byte) 50);
        Review reviewTwo = new Review(2L, "test", new Date(), user, game, (byte) 50);
        reviews.add(reviewOne);
        reviews.add(reviewTwo);

        List<ReviewResponse> reviewResponses = new ArrayList<>();
        reviewResponses.add(new ReviewResponse(reviewOne.getId(), reviewOne.getContent(), reviewOne.getCreationDate(), reviewOne.getGame(), reviewOne.getUser().getUsername(), reviewOne.getRating()));
        reviewResponses.add(new ReviewResponse(reviewTwo.getId(), reviewTwo.getContent(), reviewTwo.getCreationDate(), reviewTwo.getGame(), reviewTwo.getUser().getUsername(), reviewTwo.getRating()));

        when(reviewService.findByGameId(1)).thenReturn(reviews);
        when(reviewToReviewResponseTranslatorProvider.translate(reviews)).thenReturn(reviewResponses);

        mockMvc.perform(get("/reviews/findByGameId/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(reviewResponses)));
    }

    @Test
    public void findByUserIdAndGameIdTest() throws Exception {

        Review reviewOne = new Review(1L, "test", new Date(), new User(), new Game(), (byte) 50);
        ReviewResponse reviewResponse = new ReviewResponse(reviewOne.getId(), reviewOne.getContent(), reviewOne.getCreationDate(), reviewOne.getGame(), reviewOne.getUser().getUsername(), reviewOne.getRating());

        when(reviewService.findByUserIdAndGameId(1L, 1)).thenReturn(reviewOne);
        when(reviewToReviewResponseTranslatorProvider.translate(reviewOne)).thenReturn(reviewResponse);

        mockMvc.perform(get("/reviews/findByUserIdAndGameId/{userId}/{gameId}", 1L, 1))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(reviewResponse)));
    }

    @Test
    public void getAverageGameRatingTest() throws Exception {

        Game game = new Game(1, "test", 1, new Date(), new Date(), "test", "test");
        User user = new User(1L, "test1", "test@test", "123456", "USER", true);

        List<Review> reviews = new ArrayList<>();
        Review reviewOne = new Review(1L, "test", new Date(), user, game, (byte) 50);
        Review reviewTwo = new Review(2L, "test", new Date(), user, game, (byte) 50);
        reviews.add(reviewOne);
        reviews.add(reviewTwo);

        when(reviewService.findByGameId(1)).thenReturn(reviews);
        when(averageGameRatingCalculatorProvider.calculateAverageRating(reviews)).thenReturn((byte) 50);

        mockMvc.perform(get("/reviews/averageGameRating/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("50"));
    }

    @Test
    public void findByGameIdAndOrderByLikesTest() throws Exception {

        Game game = new Game(1, "test", 1, new Date(), new Date(), "test", "test");
        User user = new User(1L, "test1", "test@test", "123456", "USER", true);

        List<Review> reviews = new ArrayList<>();
        Review reviewOne = new Review(1L, "test", new Date(), user, game, (byte) 50);
        Review reviewTwo = new Review(2L, "test", new Date(), user, game, (byte) 50);
        reviews.add(reviewOne);
        reviews.add(reviewTwo);

        List<ReviewResponse> reviewResponses = new ArrayList<>();
        reviewResponses.add(new ReviewResponse(reviewOne.getId(), reviewOne.getContent(), reviewOne.getCreationDate(), reviewOne.getGame(), reviewOne.getUser().getUsername(), reviewOne.getRating()));
        reviewResponses.add(new ReviewResponse(reviewTwo.getId(), reviewTwo.getContent(), reviewTwo.getCreationDate(), reviewTwo.getGame(), reviewTwo.getUser().getUsername(), reviewTwo.getRating()));

        when(reviewToReviewResponseTranslatorProvider.translate(reviews)).thenReturn(reviewResponses);
        when(reviewService.findTopLikedReviewsByGameId(1, PageRequest.of(0, 10))).thenReturn(reviews);

        mockMvc.perform(get("/reviews/findByGameIdAndOrderByLikes/{gameId}/{pageNumber}", 1, 0))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(reviewResponses)));
    }
}
