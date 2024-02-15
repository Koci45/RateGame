package com.KociApp.RateGame.review;

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


}
