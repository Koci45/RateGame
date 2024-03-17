package com.KociApp.RateGame.review.helperClasses;

import com.KociApp.RateGame.review.Review;

import java.util.List;

public interface AverageGameRatingCalculatorProvider {

    byte calculateAverageRating(List<Review> reviews);
}
