package com.KociApp.RateGame.review.likes;

import com.KociApp.RateGame.user.User;
import com.KociApp.RateGame.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class ReviewLikeService implements IReviewLikeService{

    private final ReviewLikeRepository repository;
    private final UserService userService;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> user = userService.findByEmail(username);
        reviewLike.setUser(user.orElse(null));

        //deleting old like/dislike id one exists so that one user can leave only one like/dislike and not both or more
        Optional<ReviewLike> oldReviewLike = repository.findByUserIdAndReviewId(user.get().getId(), reviewLike.getReview().getId());
        if(oldReviewLike.isPresent()){
            repository.delete(oldReviewLike.orElse(null));
        }

        return repository.save(reviewLike);
    }
}
