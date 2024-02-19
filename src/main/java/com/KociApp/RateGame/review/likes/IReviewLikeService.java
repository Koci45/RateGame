package com.KociApp.RateGame.review.likes;

public interface IReviewLikeService {

    int countLikesByReviewId(Long id);

    int countDislikesByReviewId(Long id);

    ReviewLike createLikeDislike(ReviewLike reviewLike);
}
