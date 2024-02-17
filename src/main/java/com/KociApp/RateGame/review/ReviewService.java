package com.KociApp.RateGame.review;

import com.KociApp.RateGame.exception.review.ReviewAlreadyWrittenForThatGameByThatUserException;
import com.KociApp.RateGame.exception.user.UserNotFoundException;
import com.KociApp.RateGame.user.User;
import com.KociApp.RateGame.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ReviewService implements IReviewService{

    private final ReviewRepository repository;
    private final UserService userService;
    @Override
    public Optional<Review> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Review save(Review review) {
        //Assigning the author of review
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> user = userService.findByEmail(username);
        review.setUser(user.orElse(null));

        review.setCreationDate(new Date());

        //checking if this user already has reviewed this game
        Optional<Review> reviewDb = repository.findByUserIdAndGameId(user.get().getId(), review.getGame().getId());

        if(reviewDb.isPresent()){
            throw new ReviewAlreadyWrittenForThatGameByThatUserException("This game-" + review.getGame().getId() +" has been already reviewed by user-" + user.get().getId());
        }

        //checking if rating is beetwen 0 and 100
        if(review.getRating() < 0 || review.getRating() > 100){
            throw new IllegalArgumentException("Rating must be beeetwen 0 and 100");
        }

        return repository.save(review);
    }

    @Override
    public List<Review> getReviews() {
        return repository.findAll();
    }

    @Override
    public String remove(Long id) {
        Optional<Review> review = repository.findById(id);
        repository.delete(review.orElseThrow());
        return "Review -" + id + " removed";
    }

    @Override
    public List<Review> findByUserId(Long id) {

        Optional<User> user = userService.findById(id);

        if(!user.isPresent()){
            throw new UserNotFoundException("User with id-" + id + " not found");
        }
        return repository.findByUserId(id);
    }

    @Override
    public List<Review> findByGameId(int id) {
        return repository.findByGameId(id);
    }

    @Override
    public Optional<Review> findByUserIdAndGameId(Long userId, int gameId) {
        return repository.findByUserIdAndGameId(userId, gameId);
    }


}
