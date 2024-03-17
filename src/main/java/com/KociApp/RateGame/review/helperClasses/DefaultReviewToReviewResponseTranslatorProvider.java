package com.KociApp.RateGame.review.helperClasses;

import com.KociApp.RateGame.review.Review;
import com.KociApp.RateGame.review.ReviewResponse;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Primary
@NoArgsConstructor
public class DefaultReviewToReviewResponseTranslatorProvider implements ReviewToReviewResponseTranslatorProvider{

    //this class is used for translating reviews from database object format to a format without any critical information like user email being sent to client

    public List<ReviewResponse> translate(List<Review> reviews){
        List<ReviewResponse> reviewResponses = new ArrayList<>();

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

    public ReviewResponse translate(Review review){
            return new ReviewResponse(
                    review.getId(),
                    review.getContent(),
                    review.getCreationDate(),
                    review.getGame(),
                    review.getUser().getUsername(),
                    review.getRating()
            );
    }
}
