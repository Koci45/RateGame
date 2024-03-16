package com.KociApp.RateGame.review.reports;

import com.KociApp.RateGame.review.Review;
import com.KociApp.RateGame.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewReportsRepository extends JpaRepository<ReviewReport, Long> {

    Optional<ReviewReport> findByUserAndReview(User user, Review review);

    int countByReview_Id(Long reviewId);

    void deleteAllByUser_Id(Long id);

    @Query("SELECT DISTINCT rr.review.id FROM ReviewReport rr")
    List<Long> findUniqueReviewIds();
}
