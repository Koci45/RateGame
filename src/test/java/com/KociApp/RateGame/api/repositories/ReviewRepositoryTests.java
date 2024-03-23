package com.KociApp.RateGame.api.repositories;

import com.KociApp.RateGame.game.Game;
import com.KociApp.RateGame.review.Review;
import com.KociApp.RateGame.review.ReviewRepository;
import com.KociApp.RateGame.review.likes.ReviewLike;
import com.KociApp.RateGame.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReviewRepositoryTests {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void ReviewRepository_findByUserId_ReturnByUserId(){

        User userOne = new User();
        userOne.setId(1L);
        userOne.setEmail("test@email.com");
        userOne.setEnabled(true);
        userOne.setPassword("123456");
        userOne.setUsername("test");
        userOne.setRole("TEST");

        entityManager.merge(userOne);

        User userTwo = new User();
        userTwo.setId(2L);
        userTwo.setEmail("test2@email.com");
        userTwo.setEnabled(true);
        userTwo.setPassword("123456");
        userTwo.setUsername("test");
        userTwo.setRole("TEST");

        entityManager.merge(userTwo);

        Game gameOne = new Game();
        gameOne.setId(1);
        gameOne.setTitle("test1");
        gameOne.setPlatforms("test");
        gameOne.setGenres("test");
        gameOne.setCreatedAt(new Date());
        gameOne.setReleaseDate(new Date());

        entityManager.merge(gameOne);

        Game gameTwo = new Game();
        gameTwo.setId(1);
        gameTwo.setTitle("test1");
        gameTwo.setPlatforms("test");
        gameTwo.setGenres("test");
        gameTwo.setCreatedAt(new Date());
        gameTwo.setReleaseDate(new Date());

        entityManager.merge(gameTwo);

        //creating review by user one for game one
        Review reviewOne = new Review();
        reviewOne.setUser(userOne);
        reviewOne.setContent("UserOneGameOne");
        reviewOne.setCreationDate(new Date());
        reviewOne.setGame(gameOne);
        reviewOne.setRating((byte) 50);

        reviewRepository.save(reviewOne);

        //creating review by user one for game two
        Review reviewTwo = new Review();
        reviewTwo.setUser(userOne);
        reviewTwo.setContent("UserOneGameTwo");
        reviewTwo.setCreationDate(new Date());
        reviewTwo.setGame(gameTwo);
        reviewTwo.setRating((byte) 50);

        reviewRepository.save(reviewTwo);



        List<Review> foundReviews = reviewRepository.findByUserId(1L);

        Assertions.assertThat(foundReviews.size()).isEqualTo(2);
        Assertions.assertThat(foundReviews.get(0).getUser().getId()).isEqualTo(1L);
        Assertions.assertThat(foundReviews.get(1).getUser().getId()).isEqualTo(1L);
    }

    @Test
    public void ReviewRepository_findByGameId_ReturnsByGameId(){

        User user = new User();
        user.setId(1L);
        user.setEmail("test@email.com");
        user.setEnabled(true);
        user.setPassword("123456");
        user.setUsername("test");
        user.setRole("TEST");

        entityManager.merge(user);

        User userTwo = new User();
        userTwo.setId(2L);
        userTwo.setEmail("test2@email.com");
        userTwo.setEnabled(true);
        userTwo.setPassword("123456");
        userTwo.setUsername("test");
        userTwo.setRole("TEST");

        entityManager.merge(userTwo);

        Game game = new Game();
        game.setId(1);
        game.setTitle("test1");
        game.setPlatforms("test");
        game.setGenres("test");
        game.setCreatedAt(new Date());
        game.setReleaseDate(new Date());

        entityManager.merge(game);

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

    @Test
    public void ReviewRepository_findByUserIdAndGameId(){

        //creating first user
        User userOne = new User();
        userOne.setEmail("test1@email.com");
        userOne.setEnabled(true);
        userOne.setPassword("123456");
        userOne.setUsername("test");
        userOne.setRole("TEST");

        entityManager.merge(userOne);

        //creating second user
        User userTwo = new User();
        userTwo.setEmail("test2@email.com");
        userTwo.setEnabled(true);
        userTwo.setPassword("123456");
        userTwo.setUsername("test");
        userTwo.setRole("TEST");

        entityManager.merge(userTwo);

        //creating first game
        Game gameOne = new Game();
        gameOne.setId(1);
        gameOne.setTitle("test1");
        gameOne.setPlatforms("test");
        gameOne.setGenres("test");
        gameOne.setCreatedAt(new Date());
        gameOne.setReleaseDate(new Date());

        entityManager.merge(gameOne);

        //creating second game
        Game gameTwo = new Game();
        gameTwo.setId(2);
        gameTwo.setTitle("test2");
        gameTwo.setPlatforms("test");
        gameTwo.setGenres("test");
        gameTwo.setCreatedAt(new Date());
        gameTwo.setReleaseDate(new Date());

        entityManager.merge(gameTwo);

        //creating review by user one for game one
        Review reviewOne = new Review();
        reviewOne.setUser(userOne);
        reviewOne.setContent("UserOneGameOne");
        reviewOne.setCreationDate(new Date());
        reviewOne.setGame(gameOne);
        reviewOne.setRating((byte) 50);

        reviewRepository.save(reviewOne);

        //creating review by user one for game two
        Review reviewTwo = new Review();
        reviewTwo.setUser(userOne);
        reviewTwo.setContent("UserOneGameTwo");
        reviewTwo.setCreationDate(new Date());
        reviewTwo.setGame(gameTwo);
        reviewTwo.setRating((byte) 50);

        reviewRepository.save(reviewTwo);

        //creating review by user two for game one
        Review reviewThree = new Review();
        reviewThree.setUser(userTwo);
        reviewThree.setContent("UserTwoGameOne");
        reviewThree.setCreationDate(new Date());
        reviewThree.setGame(gameOne);
        reviewThree.setRating((byte) 50);

        reviewRepository.save(reviewThree);

        //creating review by user two for game two
        Review reviewFour = new Review();
        reviewFour.setUser(userTwo);
        reviewFour.setContent("UserTwoGameTwo");
        reviewFour.setCreationDate(new Date());
        reviewFour.setGame(gameTwo);
        reviewFour.setRating((byte) 50);

        reviewRepository.save(reviewFour);

        Optional<Review> resultReview = reviewRepository.findByUserIdAndGameId(userOne.getId(), gameOne.getId());

        Assertions.assertThat(resultReview.isPresent()).isEqualTo(true);
        Assertions.assertThat(resultReview.get().getUser().equals(userOne)).isEqualTo(true);
        Assertions.assertThat(resultReview.get().getGame().equals(gameOne)).isEqualTo(true);
    }

    @Test
    public void ReviewRepository_findTopLikedReviewsByGameId(){

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
        }

        reviewRepository.saveAll(Arrays.stream(reviews).toList());

        for(int i = 0; i < 30; i++){
             User user = users[i];

             for(int j = 0; j < 30 - i; j++){
                 Optional<Review> review = reviewRepository.findById((long) (j + 1));
                 ReviewLike like = new ReviewLike();
                 like.setUser(user);
                 like.setLikeDislike(true);
                 like.setReview(review.get());
                 entityManager.merge(like);
             }
        }

        List<Review> resultReviewsPageOne = reviewRepository.findTopLikedReviewsByGameId(gameOne.getId(), PageRequest.of(0, 10));
        List<Review> resultReviewsPageThree = reviewRepository.findTopLikedReviewsByGameId(gameOne.getId(), PageRequest.of(2, 10));

        Assertions.assertThat(resultReviewsPageOne.size()).isEqualTo(10);
        Assertions.assertThat(resultReviewsPageThree.size()).isEqualTo(10);
        Assertions.assertThat(resultReviewsPageOne.get(0).getUser().equals(users[0])).isEqualTo(true);
        Assertions.assertThat(resultReviewsPageOne.get(1).getUser().equals(users[1])).isEqualTo(true);
        Assertions.assertThat(resultReviewsPageThree.get(1).getUser().equals(users[21])).isEqualTo(true);
    }
}
