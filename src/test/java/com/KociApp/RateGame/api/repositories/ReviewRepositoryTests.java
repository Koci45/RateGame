package com.KociApp.RateGame.api.repositories;

import com.KociApp.RateGame.game.Game;
import com.KociApp.RateGame.game.GameRepository;
import com.KociApp.RateGame.review.Review;
import com.KociApp.RateGame.review.ReviewRepository;
import com.KociApp.RateGame.review.likes.ReviewLikeRepository;
import com.KociApp.RateGame.user.User;
import com.KociApp.RateGame.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ReviewRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ReviewLikeRepository reviewLikeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void ReviewRepository_findByUserId_ReturnByUserId(){

        User user = new User();
        user.setEmail("test@email.com");
        user.setEnabled(true);
        user.setPassword("123456");
        user.setUsername("test");
        user.setRole("TEST");

        user = userRepository.save(user);

        Game game = new Game();
        game.setId(1);
        game.setTitle("test");
        game.setPlatforms("test");
        game.setGenres("test");
        game.setCreatedAt(new Date());
        game.setReleaseDate(new Date());

        game = gameRepository.save(game);

        Game gameTwo = new Game();

        gameTwo.setId(2);
        gameTwo.setTitle("test");
        gameTwo.setPlatforms("test");
        gameTwo.setGenres("test");
        gameTwo.setCreatedAt(new Date());
        gameTwo.setReleaseDate(new Date());

        gameTwo = gameRepository.save(gameTwo);

        Review review = new Review();
        review.setUser(user);
        review.setContent("test");
        review.setCreationDate(new Date());
        review.setGame(game);
        review.setRating((byte) 50);

        reviewRepository.save(review);

        Review reviewTwo = new Review();
        reviewTwo.setUser(user);
        reviewTwo.setContent("test2");
        reviewTwo.setCreationDate(new Date());
        reviewTwo.setGame(gameTwo);
        reviewTwo.setRating((byte) 50);

        reviewRepository.save(reviewTwo);


        List<Review> foundReviews = reviewRepository.findByUserId(user.getId());

        Assertions.assertThat(foundReviews.size()).isEqualTo(2);
    }

    @Test
    public void ReviewRepository_findByGameId_ReturnsByGameId(){

        User user = new User();
        user.setEmail("test@email.com");
        user.setEnabled(true);
        user.setPassword("123456");
        user.setUsername("test");
        user.setRole("TEST");

        user = userRepository.save(user);

        User userTwo = new User();
        userTwo.setEmail("test2@email.com");
        userTwo.setEnabled(true);
        userTwo.setPassword("123456");
        userTwo.setUsername("test");
        userTwo.setRole("TEST");

        userTwo = userRepository.save(userTwo);

        Game game = new Game();
        game.setId(1);
        game.setTitle("test1");
        game.setPlatforms("test");
        game.setGenres("test");
        game.setCreatedAt(new Date());
        game.setReleaseDate(new Date());

        game = gameRepository.save(game);

        Review review = new Review();
        review.setUser(user);
        review.setContent("test");
        review.setCreationDate(new Date());
        review.setGame(game);
        review.setRating((byte) 50);

        reviewRepository.save(review);

        Review reviewTwo = new Review(); // Create a new Review object
        reviewTwo.setUser(userTwo); // Set the second user for this review
        reviewTwo.setContent("test2");
        reviewTwo.setCreationDate(new Date());
        reviewTwo.setGame(game);
        reviewTwo.setRating((byte) 50);

        reviewRepository.save(reviewTwo); // Save the second review

        List<Review> foundReviews = reviewRepository.findByGameId(game.getId());

        Assertions.assertThat(foundReviews.size()).isEqualTo(2);
    }
}
