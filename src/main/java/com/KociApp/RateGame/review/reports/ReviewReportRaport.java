package com.KociApp.RateGame.review.reports;

public record ReviewReportRaport(
        Long reviewId,
        String reviewContent,
        int reviewReportsCount,
        Long userId
) {
}
