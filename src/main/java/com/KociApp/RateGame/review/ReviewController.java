package com.KociApp.RateGame.review;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService service;
    private final ReviewToReviewResponseTranslator translator;

    @PostMapping
    public Review createReview(@RequestBody Review review){
        return service.save(review);
    }

    @GetMapping
    public List<ReviewResponse> getReviews(){
        List<Review> reviews = service.getReviews();
        //Creating separete objects for response to awoid sending crtical user information that is stored inside normal review object

        return translator.translate(reviews);
    }

    @DeleteMapping("/deleteById/{id}")
    public String deleteReview(@PathVariable Long id){
        return service.remove(id);
    }

    @GetMapping("/findByUserId/{id}")
    public List<ReviewResponse> findByUserId(@PathVariable Long id){
        List<Review> reviews = service.findByUserId(id);
        //Creating separete objects for response to awoid sending crtical user information that is stored inside normal review object

        return translator.translate(reviews);
    }

    @GetMapping("/findByGameId/{id}")
    public List<ReviewResponse> findByGameId(@PathVariable int id){
        List<Review> reviews = service.findByGameId(id);
        //Creating separete objects for response to awoid sending crtical user information that is stored inside normal review object

        return translator.translate(reviews);
    }
}
