package com.KociApp.RateGame.review;

import com.KociApp.RateGame.exception.review.ReviewAlreadyWrittenForThatGameByThatUserException;
import com.KociApp.RateGame.game.Game;
import com.KociApp.RateGame.game.GameService;
import com.KociApp.RateGame.user.User;
import com.KociApp.RateGame.user.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    private final GameService gameService;
    @Override
    public Review findById(Long id) {
        Optional<Review> review = repository.findById(id);

        if(review.isEmpty()){
            throw new EntityNotFoundException("Review wit id: " + id.toString() + " not found");
        }

        return review.get();
    }

    @Override
    public Review save(Review review) {
        //Setting id to zero to ensure its saved as new and won't replace any other
        review.setId(0L);
        //Assigning the author of review
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.findByEmail(username);
        review.setUser(user);

        review.setCreationDate(new Date());

        //checking if this user already has reviewed this game
        Optional<Review> reviewDb = repository.findByUserIdAndGameId(user.getId(), review.getGame().getId());

        if(reviewDb.isPresent()){
            throw new EntityExistsException("This game-" + review.getGame().getId() +" has been already reviewed by user-" + user.getId());
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
        Review review = findById(id);

        repository.delete(review);
        return "Review -" + id + " removed";
    }

    @Override
    public List<Review> findByUserId(Long id) {

        User user = userService.findById(id);//just to check if that user exists, if not this method will throw aprioprate exception

        return repository.findByUserId(id);
    }

    @Override
    public List<Review> findByGameId(int id) {

        Game game = gameService.findById(id);//just to check if that game exists, if not this method will throw aprioprate exception

        return repository.findByGameId(id);
    }

    @Override
    public Review findByUserIdAndGameId(Long userId, int gameId) {

        Optional<Review> review = repository.findByUserIdAndGameId(userId, gameId);

        if(review.isEmpty()){
            throw new EntityNotFoundException("A review for game with id: " + gameId + " written by user with id: " + userId.toString() + " doesn't exist");
        }

        return review.get();
    }

    @Override
    public List<Review> findTopLikedReviewsByGameId(int gameId, Pageable pageable) {
        return repository.findTopLikedReviewsByGameId(gameId, pageable);
    }


}
