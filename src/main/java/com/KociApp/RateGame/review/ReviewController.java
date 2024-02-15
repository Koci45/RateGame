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

    @PostMapping
    public Review createReview(@RequestBody Review review){
        return service.save(review);
    }

    @GetMapping
    public List<ReviewResponse> getReviews(){
        List<Review> reviews = service.getReviews();
        List<ReviewResponse> reviewResponses = new ArrayList<>();

        //Creating separete object for response to awoid sending crtical user information that is stored inside normal review object
        for(Review review : reviews){
            reviewResponses.add(new ReviewResponse(
                    review.getId(),
                    review.getContent(),
                    review.getCreationDate(),
                    review.getGame(),
                    review.getUser().getUsername(),
                    review.getRating()
            ));
        }
        return reviewResponses;
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
