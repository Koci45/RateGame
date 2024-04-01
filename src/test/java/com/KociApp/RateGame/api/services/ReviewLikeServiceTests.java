package com.KociApp.RateGame.api.services;

import com.KociApp.RateGame.game.Game;
import com.KociApp.RateGame.review.Review;
import com.KociApp.RateGame.review.likes.ReviewLike;
import com.KociApp.RateGame.review.likes.ReviewLikeRepository;
import com.KociApp.RateGame.review.likes.ReviewLikeService;
import com.KociApp.RateGame.review.reports.ReviewReport;
import com.KociApp.RateGame.user.User;
import com.KociApp.RateGame.user.UserInfo.LoggedInUserProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewLikeServiceTests {

    @Mock
    private ReviewLikeRepository reviewLikeRepository;

    @Mock
    private LoggedInUserProvider loggedInUserProvider;

    @InjectMocks
    private ReviewLikeService reviewLikeService;

    @Test
    public void createLikeDislikeWithNotExistingLikeDislike(){

        User user = new User(1L, "testt", "test@test", "123456", "USER", true);
        Review review = new Review(1L, "test", new Date(), new User(), new Game(), (byte) 50);

        ReviewLike newLike = new ReviewLike(1L, true, null, review);

        when(loggedInUserProvider.getLoggedUser()).thenReturn(user);

        reviewLikeService.createLikeDislike(newLike);

        ArgumentCaptor<ReviewLike> reviewLikeCaptor = ArgumentCaptor.forClass(ReviewLike.class);
        verify(reviewLikeRepository, times(1)).save(reviewLikeCaptor.capture());

        Assertions.assertThat(reviewLikeCaptor.getValue().getUser()).isEqualTo(user);
        Assertions.assertThat(reviewLikeCaptor.getValue().getReview()).isEqualTo(review);
        Assertions.assertThat(reviewLikeCaptor.getValue().isLikeDislike()).isTrue();
        Assertions.assertThat(reviewLikeCaptor.getValue().getId()).isEqualTo(0L);
    }

    @Test
    public void createLikeDislikeWithExistingLikeDislike(){

        User user = new User(1L, "testt", "test@test", "123456", "USER", true);
        Review review = new Review(1L, "test", new Date(), new User(), new Game(), (byte) 50);

        ReviewLike oldDislike = new ReviewLike(1L, false, user, review);
        ReviewLike newLike = new ReviewLike(1L, true, null, review);

        when(reviewLikeRepository.findByUserIdAndReviewId(user.getId(), review.getId())).thenReturn(Optional.of(oldDislike));
        when(loggedInUserProvider.getLoggedUser()).thenReturn(user);

        reviewLikeService.createLikeDislike(newLike);

        ArgumentCaptor<ReviewLike> reviewLikeCaptor = ArgumentCaptor.forClass(ReviewLike.class);
        verify(reviewLikeRepository, times(1)).save(reviewLikeCaptor.capture());
        verify(reviewLikeRepository, times(1)).delete(oldDislike);

        Assertions.assertThat(reviewLikeCaptor.getValue().getUser()).isEqualTo(user);
        Assertions.assertThat(reviewLikeCaptor.getValue().getReview()).isEqualTo(review);
        Assertions.assertThat(reviewLikeCaptor.getValue().isLikeDislike()).isTrue();
        Assertions.assertThat(reviewLikeCaptor.getValue().getId()).isEqualTo(0L);
    }
}
