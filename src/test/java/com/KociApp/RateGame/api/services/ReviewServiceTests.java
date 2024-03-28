package com.KociApp.RateGame.api.services;

import com.KociApp.RateGame.game.Game;
import com.KociApp.RateGame.game.GameRepository;
import com.KociApp.RateGame.review.Review;
import com.KociApp.RateGame.review.ReviewRepository;
import com.KociApp.RateGame.review.ReviewRequest;
import com.KociApp.RateGame.review.ReviewService;
import com.KociApp.RateGame.user.User;
import com.KociApp.RateGame.user.UserInfo.LoggedInUserProvider;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTests {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private LoggedInUserProvider loggedInUserProvider;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    public void findByIdWithExistingIdTest(){

        Review review = new Review(1L, "test", new Date(), new User(), new Game(), (byte) 50);

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        Review result = reviewService.findById(1L);

        Assertions.assertThat(result.getId()).isEqualTo(review.getId());
        Assertions.assertThat(result.getUser()).isEqualTo(review.getUser());
        Assertions.assertThat(result.getGame()).isEqualTo(review.getGame());
        Assertions.assertThat(result.getRating()).isEqualTo(review.getRating());
        Assertions.assertThat(result.getContent()).isEqualTo(review.getContent());
        Assertions.assertThat(result.getCreationDate()).isEqualTo(review.getCreationDate());
    }

    @Test
    public void findByIdWithNotExistingIdTest(){

        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> reviewService.findById(1L)).isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Review wit id: 1 not found");
    }

    @Test
    public void saveWithExistingGameAndNotExistingReviewByThatUserForThatGameWithCorrectRatingTest(){

        User user = new User(1L, "test", "test@test1", "123456", "USER", true);

        Game game = new Game();
        game.setId(1);
        ReviewRequest reviewRequest = new ReviewRequest("test", (byte) 50, 1);

        Review expectedReview = new Review(1L, "test", new Date(), user, game, (byte) 50);

        when(gameRepository.findById(anyInt())).thenReturn(Optional.of(game));
        when(loggedInUserProvider.getLoggedUser()).thenReturn(user);
        when(reviewRepository.findByUserIdAndGameId(anyLong(), anyInt())).thenReturn(Optional.empty());
        when(reviewRepository.save(any())).thenReturn(expectedReview);

        Review result = reviewService.save(reviewRequest);

        ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository, times(1)).save(reviewCaptor.capture());

        Assertions.assertThat(result.getId()).isEqualTo(1L);
        Assertions.assertThat(result.getUser()).isEqualTo(user);
        Assertions.assertThat(result.getGame()).isEqualTo(game);
        Assertions.assertThat(result.getRating()).isEqualTo(reviewRequest.rating());
        Assertions.assertThat(result.getContent()).isEqualTo(reviewRequest.content());

        //asserting that the method assign correct values that it is in charge of assigning
        Assertions.assertThat(reviewCaptor.getValue().getId()).isEqualTo(0L);
        Assertions.assertThat(reviewCaptor.getValue().getUser()).isEqualTo(user);
        Assertions.assertThat(reviewCaptor.getValue().getCreationDate()).isNotNull();
    }

    @Test
    public void saveWithExistingGameAndNotExistingReviewByThatUserForThatGameWithIncorrectRatingTest(){

        User user = new User(1L, "test", "test@test1", "123456", "USER", true);

        Game game = new Game();
        game.setId(1);
        ReviewRequest reviewRequest = new ReviewRequest("test", (byte) 150, 1);

        when(gameRepository.findById(anyInt())).thenReturn(Optional.of(game));
        when(loggedInUserProvider.getLoggedUser()).thenReturn(user);
        when(reviewRepository.findByUserIdAndGameId(anyLong(), anyInt())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> reviewService.save(reviewRequest)).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Rating must be between 0 and 100");

    }

    @Test
    public void saveWithExistingGameAndExistingReviewByThatUserForThatGameWithCorrectRatingTest(){

        User user = new User(1L, "test", "test@test1", "123456", "USER", true);

        Game game = new Game();
        game.setId(1);
        ReviewRequest reviewRequest = new ReviewRequest("test", (byte) 50, 1);

        Review expectedReview = new Review(1L, "test", new Date(), user, game, (byte) 50);

        when(gameRepository.findById(anyInt())).thenReturn(Optional.of(game));
        when(loggedInUserProvider.getLoggedUser()).thenReturn(user);
        when(reviewRepository.findByUserIdAndGameId(anyLong(), anyInt())).thenReturn(Optional.of(expectedReview));

        Assertions.assertThatThrownBy(() -> reviewService.save(reviewRequest)).isInstanceOf(EntityExistsException.class)
                .hasMessageContaining("This game-" + game.getId() +" has been already reviewed by user-" + user.getId());
    }

    @Test
    public void saveWithNotExistingGameAndNotExistingReviewByThatUserForThatGameWithCorrectRatingTest(){

        ReviewRequest reviewRequest = new ReviewRequest("test", (byte) 50, 1);

        when(gameRepository.findById(anyInt())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> reviewService.save(reviewRequest)).isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Game with id" + reviewRequest.gameId() + "not found");

    }

    @Test
    public void getReviewsTest(){

        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review(1L, "test", new Date(), new User(), new Game(), (byte) 50));
        reviews.add(new Review(2L, "test", new Date(), new User(), new Game(), (byte) 50));

        when(reviewRepository.findAll()).thenReturn(reviews);

        List<Review> result = reviewService.getReviews();

        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result.get(0).getId()).isEqualTo(1);
        Assertions.assertThat(result.get(1).getId()).isEqualTo(2);
    }

    @Test
    public void removeReviewWithExistingIdTest(){

        Review review = new Review(1L, "test", new Date(), new User(), new Game(), (byte) 50);

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        String result = reviewService.remove(1L);

        verify(reviewRepository, times(1)).delete(review);
        Assertions.assertThat(result).isEqualTo("Review -" + review.getId() + " removed");
    }

    @Test
    public void removeReviewWithNotExistingIdTest(){

        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> reviewService.remove(1L)).isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("not found");
    }

    @Test
    public void findByUserIdAndGameIdWithExistingUserIdAndExistingGameIdTest(){

        User user = new User(1L, "test", "test@test1", "123456", "USER", true);
        Game game = new Game();
        game.setId(1);

        Review review = new Review(1L, "test", new Date(), user, game, (byte) 50);

        when(reviewRepository.findByUserIdAndGameId(1L, 1)).thenReturn(Optional.of(review));

        Review result = reviewService.findByUserIdAndGameId(user.getId(), game.getId());

        Assertions.assertThat(result.getId()).isEqualTo(1L);
        Assertions.assertThat(result.getGame()).isEqualTo(game);
        Assertions.assertThat(result.getUser()).isEqualTo(user);
    }

    @Test
    public void findByUserIdAndGameIdWithNotExistingUserIdOrNotExistingGameIdTest(){

        when(reviewRepository.findByUserIdAndGameId(anyLong(), anyInt())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> reviewService.findByUserIdAndGameId(1L, 1)).isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("A review for game with id: 1 written by user with id: 1 doesn't exist");
    }

}
