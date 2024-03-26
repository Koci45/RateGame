package com.KociApp.RateGame.other;

import com.KociApp.RateGame.game.Game;
import com.KociApp.RateGame.review.Review;
import com.KociApp.RateGame.review.ReviewResponse;
import com.KociApp.RateGame.review.helperClasses.DefaultReviewToReviewResponseTranslatorProvider;
import com.KociApp.RateGame.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class DefaultReviewToReviewResponseTranslatorProviderProviderTests {

    @Autowired
    private DefaultReviewToReviewResponseTranslatorProvider translator;

    @Test
    public void translateSingleReview(){

        User user = new User();
        user.setEmail("test@email.com");
        user.setEnabled(true);
        user.setPassword("123456");
        user.setUsername("test");
        user.setRole("TEST");

        Game game = new Game();
        game.setId(1);
        game.setTitle("test");
        game.setPlatforms("test");
        game.setGenres("test");
        game.setCreatedAt(new Date());
        game.setReleaseDate(new Date());

        Review review = new Review();
        review.setRating((byte) 50);
        review.setUser(user);
        review.setGame(game);
        review.setContent("test");
        review.setCreationDate(new Date());
        review.setId(1L);

        ReviewResponse response = translator.translate(review);

        Assertions.assertThat(response.getId()).isEqualTo(review.getId());
        Assertions.assertThat(response.getContent()).isEqualTo(review.getContent());
        Assertions.assertThat(response.getCreationDate()).isEqualTo(review.getCreationDate());
        Assertions.assertThat(response.getGame()).isEqualTo(review.getGame());
        Assertions.assertThat(response.getUsername()).isEqualTo(review.getUser().getUsername());
        Assertions.assertThat(response.getRating()).isEqualTo(review.getRating());
    }

    @Test
    public void translateListOfReviews(){

        List<Review> reviews = new ArrayList<>();

        Game game = new Game();
        game.setId(1);
        game.setTitle("test1");
        game.setPlatforms("test");
        game.setGenres("test");
        game.setCreatedAt(new Date());
        game.setReleaseDate(new Date());

        User[] users = new User[10];

        for(int i = 0; i < 10; i++){
            users[i] = new User();
            users[i].setEmail("test" + i + "@email.com");
            users[i].setEnabled(true);
            users[i].setPassword("123456");
            users[i].setUsername("test");
            users[i].setRole("TEST");
        }

        for(int i = 0; i < 10; i++){
            Review review = new Review();
            review.setId((long) i + 1);
            review.setRating((byte) 50);
            review.setUser(users[i]);
            review.setContent("test");
            review.setGame(game);
            review.setCreationDate(new Date());

            reviews.add(review);
        }

        List<ReviewResponse> responses = translator.translate(reviews);

        Assertions.assertThat(responses.size()).isEqualTo(10);


        for(int i = 0; i < 10; i++){
            Assertions.assertThat(responses.get(i).getId()).isEqualTo(reviews.get(i).getId());
            Assertions.assertThat(responses.get(i).getContent()).isEqualTo(reviews.get(i).getContent());
            Assertions.assertThat(responses.get(i).getCreationDate()).isEqualTo(reviews.get(i).getCreationDate());
            Assertions.assertThat(responses.get(i).getGame()).isEqualTo(reviews.get(i).getGame());
            Assertions.assertThat(responses.get(i).getUsername()).isEqualTo(reviews.get(i).getUser().getUsername());
            Assertions.assertThat(responses.get(i).getRating()).isEqualTo(reviews.get(i).getRating());
        }
    }
}
