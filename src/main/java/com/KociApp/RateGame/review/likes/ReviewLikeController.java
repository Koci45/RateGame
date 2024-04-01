package com.KociApp.RateGame.review.likes;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reviewLikes")
public class ReviewLikeController {

    private final ReviewLikeService service;

    @GetMapping("/likes/{id}")
    public int countLikesByReviewId(@PathVariable Long id){
        return service.countLikesByReviewId(id);
    }

    @GetMapping("/dislikes/{id}")
    public int countDislikesByReviewId(@PathVariable Long id){
        return service.countDislikesByReviewId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewLike createLikeDislike(@RequestBody ReviewLike reviewLike){
        return service.createLikeDislike(reviewLike);
    }
}
