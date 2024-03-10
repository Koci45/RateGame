package com.KociApp.RateGame.other;

import com.KociApp.RateGame.game.Game;
import com.KociApp.RateGame.review.Review;
import com.KociApp.RateGame.review.ReviewResponse;
import com.KociApp.RateGame.review.ReviewToReviewResponseTranslator;
import com.KociApp.RateGame.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Date;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReviewToReviewResponseTranslatorTests {

    @Autowired
    private ReviewToReviewResponseTranslator translator;

    @Test
    public void translateSingle(){

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
}
