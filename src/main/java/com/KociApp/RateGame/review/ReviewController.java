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

    @DeleteMapping("/deleteById/{id}")
    public String deleteReview(@PathVariable Long id){
        return service.remove(id);
    }

    @GetMapping("/findByUserId/{id}")
    public List<Review> findByUserId(@PathVariable Long id){
        return service.findByUserId(id);
    }

    @GetMapping("/findByGameId/{id}")
    public List<Review> findByGameId(@PathVariable int id){
        return service.findByGameId(id);
    }
}
