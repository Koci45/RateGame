package com.KociApp.RateGame.review;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/findByUserIdAndGameId/{userId}/{gameId}")
    public Optional<ReviewResponse> findByUserIdAndGameId(@PathVariable Long userId, @PathVariable int gameId){
        Optional<ReviewResponse> reviewResponse = Optional.ofNullable(translator.translate(service.findByUserIdAndGameId(userId, gameId)));

        return  reviewResponse;
    }

    @GetMapping("/averageGameRating/{id}")
     public byte getAverageGameRating (@PathVariable int id){
        List<Review> reviews = service.findByGameId(id);
        byte rating = AverageGameRatingCalculator.calculateAverageRating(reviews);

        return rating;
    }

    @GetMapping("/findByGameIdAndOrderByLikes/{gameId}/{pageNumber}")
    public List<ReviewResponse> findByGameIdAndOrderByLikes(@PathVariable int gameId, @PathVariable int pageNumber){
        List<Review> reviews = service.findTopLikedReviewsByGameId(gameId, PageRequest.of(pageNumber, 10));

        //Creating separete objects for response to awoid sending crtical user information that is stored inside normal review object
        return translator.translate(reviews);
    }

}
