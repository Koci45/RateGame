package com.KociApp.RateGame.review;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService service;

    @PostMapping
    public Review createReview(@RequestBody Review review){
        return service.save(review);
    }

    @GetMapping
    public List<Review> getReviews(){
        return service.getReviews();
    }
}
