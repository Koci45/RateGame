package com.KociApp.RateGame.review;

import java.util.List;
import java.util.Optional;

public interface IReviewService {

    Optional<Review> findById(Long id);

    Review save(Review review);

    List<Review> getReviews();
}
