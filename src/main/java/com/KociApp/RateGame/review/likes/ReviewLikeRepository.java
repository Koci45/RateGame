package com.KociApp.RateGame.review.likes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    @Query("SELECT COUNT(l) FROM ReviewLike l WHERE l.review.id = :reviewId AND l.likeDislike = true ")
    int countLikesByReviewId(@Param("reviewId") Long reviewId);

    @Query("SELECT COUNT(l) FROM ReviewLike l WHERE l.review.id = :reviewId AND l.likeDislike = false ")
    int countDislikesByReviewId(@Param("reviewId") Long reviewId);

    Optional<ReviewLike> findByUserIdAndReviewId(Long userId, Long reviewId);
}
