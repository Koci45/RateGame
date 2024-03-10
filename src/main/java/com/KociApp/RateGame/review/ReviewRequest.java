package com.KociApp.RateGame.review;

public record ReviewRequest(
        String content,
        byte rating,
        int gameId

) {
}
