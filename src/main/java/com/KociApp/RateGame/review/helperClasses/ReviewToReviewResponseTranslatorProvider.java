package com.KociApp.RateGame.review.helperClasses;

import com.KociApp.RateGame.review.Review;
import com.KociApp.RateGame.review.ReviewResponse;

import java.util.List;

public interface ReviewToReviewResponseTranslatorProvider {

    List<ReviewResponse> translate(List<Review> reviews);

    ReviewResponse translate(Review review);
}
