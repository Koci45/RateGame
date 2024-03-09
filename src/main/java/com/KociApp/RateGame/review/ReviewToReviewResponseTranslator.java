package com.KociApp.RateGame.review;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@NoArgsConstructor
public class ReviewToReviewResponseTranslator {

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
