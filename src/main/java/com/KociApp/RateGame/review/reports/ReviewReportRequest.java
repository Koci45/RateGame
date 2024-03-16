package com.KociApp.RateGame.review.reports;

public record ReviewReportRequest(
        String content,
        Long reviewId
) {
}
