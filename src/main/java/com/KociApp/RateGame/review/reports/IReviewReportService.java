package com.KociApp.RateGame.review.reports;

import com.KociApp.RateGame.review.Review;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface IReviewReportService {

    ReviewReport save(ReviewReportRequest reviewReportRequest);

    String remove(Long id);

    ReviewReport findById(Long id);

    List<ReviewReport> findAll();

    int countByReview(Long reviewId);

    List<Long> findUniqueReviewIds();

    List<ReviewReportRaport> raport();


}
