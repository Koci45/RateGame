package com.KociApp.RateGame.review.likes;

import com.KociApp.RateGame.user.User;
import com.KociApp.RateGame.user.UserInfo.LoggedInUserProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class ReviewLikeService implements IReviewLikeService{

    private final ReviewLikeRepository repository;
    private final LoggedInUserProvider loggedInUserProvider;

    @Override
    public int countLikesByReviewId(Long id) {

        return repository.countLikesByReviewId(id);
    }

    @Override
    public int countDislikesByReviewId(Long id) {

        return repository.countDislikesByReviewId(id);
    }

    @Override
    public ReviewLike createLikeDislike(ReviewLike reviewLike) {
        //Assigning the author of review
        User user = loggedInUserProvider.getLoggedUser();
        reviewLike.setUser(user);

        //deleting old like/dislike if one exists so that one user can leave only one like/dislike and not both or more
        Optional<ReviewLike> oldReviewLike = repository.findByUserIdAndReviewId(user.getId(), reviewLike.getReview().getId());

        if(oldReviewLike.isPresent()){
            repository.delete(oldReviewLike.get());
        }

        return repository.save(reviewLike);
    }
}
