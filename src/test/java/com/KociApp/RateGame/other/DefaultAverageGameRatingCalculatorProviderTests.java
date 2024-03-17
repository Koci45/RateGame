package com.KociApp.RateGame.other;

import com.KociApp.RateGame.review.helperClasses.DefaultAverageGameRatingCalculatorProvider;
import com.KociApp.RateGame.review.Review;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DefaultAverageGameRatingCalculatorProviderTests {

    @Autowired
    private DefaultAverageGameRatingCalculatorProvider averageGameRatingCalculatorProvider;
    @Test
    public void calculateAverageRatingTest(){


        Review[] reviews = new Review[5];
        reviews[0] = new Review();
        reviews[1] = new Review();
        reviews[2] = new Review();
        reviews[3] = new Review();
        reviews[4] = new Review();
        reviews[0].setRating((byte) 30);
        reviews[1].setRating((byte) 40);
        reviews[2].setRating((byte) 50);
        reviews[3].setRating((byte) 60);
        reviews[4].setRating((byte) 70);

        byte resultOne = averageGameRatingCalculatorProvider.calculateAverageRating(Arrays.stream(reviews).toList());

        reviews[0] = new Review();
        reviews[1] = new Review();
        reviews[2] = new Review();
        reviews[3] = new Review();
        reviews[4] = new Review();
        reviews[0].setRating((byte) 0);
        reviews[1].setRating((byte) 0);
        reviews[2].setRating((byte) 0);
        reviews[3].setRating((byte) 0);
        reviews[4].setRating((byte) 0);

        byte resultTwo = averageGameRatingCalculatorProvider.calculateAverageRating(Arrays.stream(reviews).toList());

        List<Review> emptyList = new ArrayList<>();

        byte resultThree = averageGameRatingCalculatorProvider.calculateAverageRating(emptyList);

        Assert.assertEquals(resultOne, 50);
        Assert.assertEquals(resultTwo, 0);
        Assert.assertEquals(resultThree, 0);

    }
}
