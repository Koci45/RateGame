package com.KociApp.RateGame.api.repositories;

import com.KociApp.RateGame.game.Game;
import com.KociApp.RateGame.review.Review;
import com.KociApp.RateGame.review.likes.ReviewLike;
import com.KociApp.RateGame.review.likes.ReviewLikeRepository;
import com.KociApp.RateGame.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Date;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ReviewLikeRepositoryTests {

    @Autowired
    private ReviewLikeRepository reviewLikeRepository;

    @Autowired
    TestEntityManager entityManager;

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
            entityManager.merge(users[i]);
        }


        Game gameOne = new Game();
        gameOne.setId(1);
        gameOne.setTitle("test1");
        gameOne.setPlatforms("test");
        gameOne.setGenres("test");
        gameOne.setCreatedAt(new Date());
        gameOne.setReleaseDate(new Date());

        entityManager.merge(gameOne);

        Review[] reviews = new Review[30];

        for(int i = 0; i < 30; i++){
            reviews[i] = new Review();
            reviews[i].setUser(users[i]);
            reviews[i].setContent("review" + i);
            reviews[i].setCreationDate(new Date());
            reviews[i].setGame(gameOne);
            reviews[i].setRating((byte) 50);
            entityManager.merge(reviews[i]);
        }


        for(int i = 0; i < 30; i++){
            User user = users[i];

            for(int j = 0; j < 30 - i; j++){
                Review review = reviews[i];
                ReviewLike like = new ReviewLike();
                like.setUser(user);
                like.setLikeDislike(true);
                like.setReview(review);
                reviewLikeRepository.save(like);
            }
        }

        Assertions.assertThat(reviewLikeRepository.countLikesByReviewId(reviews[0].getId())).isEqualTo(30);
        Assertions.assertThat(reviewLikeRepository.countLikesByReviewId(reviews[1].getId())).isEqualTo(29);
        Assertions.assertThat(reviewLikeRepository.countLikesByReviewId(reviews[29].getId())).isEqualTo(1);
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
            entityManager.merge(users[i]);
        }


        Game gameOne = new Game();
        gameOne.setId(1);
        gameOne.setTitle("test1");
        gameOne.setPlatforms("test");
        gameOne.setGenres("test");
        gameOne.setCreatedAt(new Date());
        gameOne.setReleaseDate(new Date());

        entityManager.merge(gameOne);

        Review[] reviews = new Review[30];

        for(int i = 0; i < 30; i++){
            reviews[i] = new Review();
            reviews[i].setUser(users[i]);
            reviews[i].setContent("review" + i);
            reviews[i].setCreationDate(new Date());
            reviews[i].setGame(gameOne);
            reviews[i].setRating((byte) 50);
            entityManager.merge(reviews[i]);
        }


        for(int i = 0; i < 30; i++){
            User user = users[i];

            for(int j = 0; j < 30 - i; j++){
                Review review = reviews[i];
                ReviewLike like = new ReviewLike();
                like.setUser(user);
                like.setLikeDislike(false);
                like.setReview(review);
                reviewLikeRepository.save(like);
            }
        }

        Assertions.assertThat(reviewLikeRepository.countDislikesByReviewId(reviews[0].getId())).isEqualTo(30);
        Assertions.assertThat(reviewLikeRepository.countDislikesByReviewId(reviews[1].getId())).isEqualTo(29);
        Assertions.assertThat(reviewLikeRepository.countDislikesByReviewId(reviews[29].getId())).isEqualTo(1);
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
            entityManager.merge(users[i]);
        }

        Game gameOne = new Game();
        gameOne.setId(1);
        gameOne.setTitle("test1");
        gameOne.setPlatforms("test");
        gameOne.setGenres("test");
        gameOne.setCreatedAt(new Date());
        gameOne.setReleaseDate(new Date());

        entityManager.merge(gameOne);

        Review[] reviews = new Review[5];

        for(int i = 0; i < 5; i++){
            reviews[i] = new Review();
            reviews[i].setUser(users[i]);
            reviews[i].setContent("review" + i);
            reviews[i].setCreationDate(new Date());
            reviews[i].setGame(gameOne);
            reviews[i].setRating((byte) 50);
            entityManager.merge(reviews[i]);
        }

        for(int i = 0; i < 5; i++){
            User user = users[i];

            Review review = reviews[i];
            ReviewLike like = new ReviewLike();
            like.setUser(user);
            like.setLikeDislike(true);
            like.setReview(review);
            reviewLikeRepository.save(like);
        }

        Assertions.assertThat(reviewLikeRepository.findByUserIdAndReviewId(1L,1L).isPresent()).isEqualTo(true);
        Assertions.assertThat(reviewLikeRepository.findByUserIdAndReviewId(1L,2L).isPresent()).isEqualTo(false);
        Assertions.assertThat(reviewLikeRepository.findByUserIdAndReviewId(1L,1L).get().getUser()).isEqualTo(users[0]);
        Assertions.assertThat(reviewLikeRepository.findByUserIdAndReviewId(1L,1L).get().getReview()).isEqualTo(reviews[0]);
        Assertions.assertThat(reviewLikeRepository.findByUserIdAndReviewId(3L,3L).get().getUser()).isEqualTo(users[2]);
        Assertions.assertThat(reviewLikeRepository.findByUserIdAndReviewId(3L,3L).get().getReview()).isEqualTo(reviews[2]);
        Assertions.assertThat(reviewLikeRepository.findByUserIdAndReviewId(5L,5L).get().getUser()).isEqualTo(users[4]);
        Assertions.assertThat(reviewLikeRepository.findByUserIdAndReviewId(5L,5L).get().getReview()).isEqualTo(reviews[4]);
    }

}
