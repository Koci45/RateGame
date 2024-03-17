package com.KociApp.RateGame.review;

import com.KociApp.RateGame.game.Game;
import com.KociApp.RateGame.game.GameRepository;
import com.KociApp.RateGame.user.User;
import com.KociApp.RateGame.user.UserInfo.LoggedInUserProvider;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ReviewService implements IReviewService{

    private final ReviewRepository repository;
    private final GameRepository gameRepository;
    private final LoggedInUserProvider loggedInUserProvider;

    @Override
    public Review findById(Long id) {
        Optional<Review> review = repository.findById(id);

        if(review.isEmpty()){
            throw new EntityNotFoundException("Review wit id: " + id + " not found");
        }

        return review.get();
    }

    @Override
    public Review save(ReviewRequest reviewRequest) {

        Review review = new Review();
        review.setId(0L);

        Optional<Game> game = gameRepository.findById(reviewRequest.gameId());

        if(game.isEmpty()){
            throw new EntityNotFoundException("Game with id" + reviewRequest.gameId() + "not found");
        }
        review.setGame(game.get());

        review.setContent(reviewRequest.content());

        review.setRating(reviewRequest.rating());

        //Assigning the author of review
        User user = loggedInUserProvider.getLoggedUser();
        review.setUser(user);

        review.setCreationDate(new Date());

        //checking if this user already has reviewed this game
        Optional<Review> reviewDb = repository.findByUserIdAndGameId(user.getId(), review.getGame().getId());

        if(reviewDb.isPresent()){
            throw new EntityExistsException("This game-" + review.getGame().getId() +" has been already reviewed by user-" + user.getId());
        }

        //checking if rating is between 0 and 100
        if(review.getRating() < 0 || review.getRating() > 100){
            throw new IllegalArgumentException("Rating must be between 0 and 100");
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

        return repository.findByUserId(id);
    }

    @Override
    public List<Review> findByGameId(int id) {

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
