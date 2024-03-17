package com.KociApp.RateGame.review;

import com.KociApp.RateGame.exception.NullFieldsException;
import com.KociApp.RateGame.review.helperClasses.AverageGameRatingCalculatorProvider;
import com.KociApp.RateGame.review.helperClasses.DefaultAverageGameRatingCalculatorProvider;
import com.KociApp.RateGame.review.helperClasses.ReviewToReviewResponseTranslatorProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService service;
    private final ReviewToReviewResponseTranslatorProvider translator;
    private final AverageGameRatingCalculatorProvider calculator;

    @PostMapping
    public Review createReview(@RequestBody ReviewRequest reviewRequest) {
        if(reviewRequest.content().isEmpty()){
            throw new NullFieldsException("Content cannot be empty");
        }
        if(reviewRequest.gameId() == 0){
            throw new NullFieldsException("GameId cannot be empty");
        }
        return service.save(reviewRequest);
    }

    @GetMapping
    public List<ReviewResponse> getReviews(){
        List<Review> reviews = service.getReviews();
        //Creating separate objects for response to avoid sending critical user information that is stored inside normal review object

        return translator.translate(reviews);
    }

    @DeleteMapping("/deleteById/{id}")
    public String deleteReview(@PathVariable Long id){
        return service.remove(id);
    }

    @GetMapping("/findByUserId/{id}")
    public List<ReviewResponse> findByUserId(@PathVariable Long id){
        List<Review> reviews = service.findByUserId(id);
        //Creating separate objects for response to avoid sending critical user information that is stored inside normal review object

        return translator.translate(reviews);
    }

    @GetMapping("/findByGameId/{id}")
    public List<ReviewResponse> findByGameId(@PathVariable int id){
        List<Review> reviews = service.findByGameId(id);
        //Creating separate objects for response to avoid sending critical user information that is stored inside normal review object

        return translator.translate(reviews);
    }

    @GetMapping("/findByUserIdAndGameId/{userId}/{gameId}")
    public ReviewResponse findByUserIdAndGameId(@PathVariable Long userId, @PathVariable int gameId){

        //Creating separate objects for response to avoid sending critical user information that is stored inside normal review object
        return translator.translate(service.findByUserIdAndGameId(userId, gameId));
    }

    @GetMapping("/averageGameRating/{id}")
     public byte getAverageGameRating (@PathVariable int id){
        List<Review> reviews = service.findByGameId(id);

        return calculator.calculateAverageRating(reviews);
    }

    @GetMapping("/findByGameIdAndOrderByLikes/{gameId}/{pageNumber}")
    public List<ReviewResponse> findByGameIdAndOrderByLikes(@PathVariable int gameId, @PathVariable int pageNumber){
        List<Review> reviews = service.findTopLikedReviewsByGameId(gameId, PageRequest.of(pageNumber, 10));

        //Creating separate objects for response to avoid sending critical user information that is stored inside normal review object
        return translator.translate(reviews);
    }

}
