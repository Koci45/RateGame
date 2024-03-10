package com.KociApp.RateGame.review;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IReviewService {

    Review findById(Long id);

    Review save(ReviewRequest reviewRequest);

    List<Review> getReviews();

    String remove(Long id);

    List<Review> findByUserId(Long id);

    List<Review> findByGameId(int id);

    Review findByUserIdAndGameId(Long userId, int gameId);

    List<Review> findTopLikedReviewsByGameId(int gameId, Pageable pageable);
}
