package com.KociApp.RateGame.review;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@NoArgsConstructor
public class ReviewToReviewResponseTranslator {

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

    public ReviewResponse translate(Optional<Review> reviewOptional){
        if (reviewOptional.isPresent()) {
            Review review = reviewOptional.get();
            return new ReviewResponse(
                    review.getId(),
                    review.getContent(),
                    review.getCreationDate(),
                    review.getGame(),
                    review.getUser().getUsername(),
                    review.getRating()
            );
        } else {
            // Return a default ReviewResponse or throw an exception, depending on your requirements
            return null; // or throw new NoSuchElementException("Review not found");
        }
    }
}
