package com.KociApp.RateGame.review.helperClasses;

import com.KociApp.RateGame.review.Review;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

@Primary
@Component
public class DefaultAverageGameRatingCalculatorProvider implements AverageGameRatingCalculatorProvider{

    public byte calculateAverageRating(List<Review> reviews){
        int avg = 0;

        for(Review review : reviews){
            avg += review.getRating();
        }

        int size = reviews.isEmpty() ? 1 : reviews.size();

        return (byte) (avg/size);
    }
}
