package com.KociApp.RateGame.review;

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


}
