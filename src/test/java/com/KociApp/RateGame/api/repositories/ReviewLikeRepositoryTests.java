package com.KociApp.RateGame.api.repositories;

import com.KociApp.RateGame.game.Game;
import com.KociApp.RateGame.game.GameRepository;
import com.KociApp.RateGame.review.Review;
import com.KociApp.RateGame.review.ReviewRepository;
import com.KociApp.RateGame.review.likes.ReviewLike;
import com.KociApp.RateGame.review.likes.ReviewLikeRepository;
import com.KociApp.RateGame.user.User;
import com.KociApp.RateGame.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ReviewLikeRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ReviewLikeRepository reviewLikeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void ReviewLikeRepository_countLikesByReviewId(){

        User[] users = new User[30];

        for(int i = 0; i < 30; i++){
            users[i] = new User();
            users[i].setEmail("test" + i + "@email.com");
            users[i].setEnabled(true);
            users[i].setPassword("123456");
            users[i].setUsername("test");
            users[i].setRole("TEST");
        }

        userRepository.saveAll(Arrays.stream(users).toList());

        Game gameOne = new Game();
        gameOne.setId(1);
        gameOne.setTitle("test1");
        gameOne.setPlatforms("test");
        gameOne.setGenres("test");
        gameOne.setCreatedAt(new Date());
        gameOne.setReleaseDate(new Date());

        gameOne = gameRepository.save(gameOne);

        Review[] reviews = new Review[30];

        for(int i = 0; i < 30; i++){
            reviews[i] = new Review();
            reviews[i].setUser(userRepository.findById((long) i + 1).orElseThrow());
            reviews[i].setContent("review" + i);
            reviews[i].setCreationDate(new Date());
            reviews[i].setGame(gameOne);
            reviews[i].setRating((byte) 50);
        }

        reviewRepository.saveAll(Arrays.stream(reviews).toList());

        for(int i = 0; i < 30; i++){
            Optional<User> user = userRepository.findById((long) (i + 1));

            for(int j = 0; j < 30 - i; j++){
                Optional<Review> review = reviewRepository.findById((long) (j + 1));
                ReviewLike like = new ReviewLike();
                like.setUser(user.get());
                like.setLikeDislike(true);
                like.setReview(review.get());
                reviewLikeRepository.save(like);
            }
        }

        Assertions.assertThat(reviewLikeRepository.countLikesByReviewId(reviewRepository.findById(1L).get().getId())).isEqualTo(30);
        Assertions.assertThat(reviewLikeRepository.countLikesByReviewId(reviewRepository.findById(2L).get().getId())).isEqualTo(29);
        Assertions.assertThat(reviewLikeRepository.countLikesByReviewId(reviewRepository.findById(30L).get().getId())).isEqualTo(1);


    }

    @Test
    public void ReviewLikeRepository_countDislikesByReviewId(){

        User[] users = new User[30];

        for(int i = 0; i < 30; i++){
            users[i] = new User();
            users[i].setEmail("test" + i + "@email.com");
            users[i].setEnabled(true);
            users[i].setPassword("123456");
            users[i].setUsername("test");
            users[i].setRole("TEST");
        }

        userRepository.saveAll(Arrays.stream(users).toList());

        Game gameOne = new Game();
        gameOne.setId(1);
        gameOne.setTitle("test1");
        gameOne.setPlatforms("test");
        gameOne.setGenres("test");
        gameOne.setCreatedAt(new Date());
        gameOne.setReleaseDate(new Date());

        gameOne = gameRepository.save(gameOne);

        Review[] reviews = new Review[30];

        for(int i = 0; i < 30; i++){
            reviews[i] = new Review();
            reviews[i].setUser(userRepository.findById((long) i + 1).orElseThrow());
            reviews[i].setContent("review" + i);
            reviews[i].setCreationDate(new Date());
            reviews[i].setGame(gameOne);
            reviews[i].setRating((byte) 50);
        }

        reviewRepository.saveAll(Arrays.stream(reviews).toList());

        for(int i = 0; i < 30; i++){
            Optional<User> user = userRepository.findById((long) (i + 1));

            for(int j = 0; j < 30 - i; j++){
                Optional<Review> review = reviewRepository.findById((long) (j + 1));
                ReviewLike like = new ReviewLike();
                like.setUser(user.get());
                like.setLikeDislike(false);
                like.setReview(review.get());
                reviewLikeRepository.save(like);
            }
        }

        Assertions.assertThat(reviewLikeRepository.countDislikesByReviewId(reviewRepository.findById(1L).get().getId())).isEqualTo(30);
        Assertions.assertThat(reviewLikeRepository.countDislikesByReviewId(reviewRepository.findById(2L).get().getId())).isEqualTo(29);
        Assertions.assertThat(reviewLikeRepository.countDislikesByReviewId(reviewRepository.findById(30L).get().getId())).isEqualTo(1);

        userRepository.deleteAll();
        reviewRepository.deleteAll();
        reviewLikeRepository.deleteAll();
    }

    @Test
    public void ReviewLikeRepository_findByUserIdAndReviewId(){

        User[] users = new User[5];

        for(int i = 0; i < 5; i++){
            users[i] = new User();
            users[i].setEmail("test" + i + "@email.com");
            users[i].setEnabled(true);
            users[i].setPassword("123456");
            users[i].setUsername("test");
            users[i].setRole("TEST");
        }

        userRepository.saveAll(Arrays.stream(users).toList());

        Game gameOne = new Game();
        gameOne.setId(1);
        gameOne.setTitle("test1");
        gameOne.setPlatforms("test");
        gameOne.setGenres("test");
        gameOne.setCreatedAt(new Date());
        gameOne.setReleaseDate(new Date());

        gameOne = gameRepository.save(gameOne);

        Review[] reviews = new Review[5];

        for(int i = 0; i < 5; i++){
            reviews[i] = new Review();
            reviews[i].setUser(userRepository.findById((long) i + 1).orElseThrow());
            reviews[i].setContent("review" + i);
            reviews[i].setCreationDate(new Date());
            reviews[i].setGame(gameOne);
            reviews[i].setRating((byte) 50);
        }

        reviewRepository.saveAll(Arrays.stream(reviews).toList());

        for(int i = 0; i < 5; i++){
            Optional<User> user = userRepository.findById((long) (i + 1));

            Optional<Review> review = reviewRepository.findById((long) (i + 1));
            ReviewLike like = new ReviewLike();
            like.setUser(user.get());
            like.setLikeDislike(true);
            like.setReview(review.get());
            reviewLikeRepository.save(like);
        }

        Assertions.assertThat(reviewLikeRepository.findByUserIdAndReviewId(1L,1L).isPresent()).isEqualTo(true);
        Assertions.assertThat(reviewLikeRepository.findByUserIdAndReviewId(1L,2L).isPresent()).isEqualTo(false);
        Assertions.assertThat(reviewLikeRepository.findByUserIdAndReviewId(1L,1L).get().getUser()).isEqualTo(userRepository.findById(1L).get());
        Assertions.assertThat(reviewLikeRepository.findByUserIdAndReviewId(1L,1L).get().getReview()).isEqualTo(reviewRepository.findById(1L).get());
        Assertions.assertThat(reviewLikeRepository.findByUserIdAndReviewId(3L,3L).get().getUser()).isEqualTo(userRepository.findById(3L).get());
        Assertions.assertThat(reviewLikeRepository.findByUserIdAndReviewId(3L,3L).get().getReview()).isEqualTo(reviewRepository.findById(3L).get());
        Assertions.assertThat(reviewLikeRepository.findByUserIdAndReviewId(5L,5L).get().getUser()).isEqualTo(userRepository.findById(5L).get());
        Assertions.assertThat(reviewLikeRepository.findByUserIdAndReviewId(5L,5L).get().getReview()).isEqualTo(reviewRepository.findById(5L).get());
    }


}
