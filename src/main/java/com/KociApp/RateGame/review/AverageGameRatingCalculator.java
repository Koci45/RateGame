package com.KociApp.RateGame.review;

import java.util.List;

public class AverageGameRatingCalculator {

    public static byte calculateAverageRating(List<Review> reviews){
        int avg = 0;

        for(Review review : reviews){
            avg += (int)review.getRating();
        }

        return (byte) (avg/reviews.size());
    }
}
