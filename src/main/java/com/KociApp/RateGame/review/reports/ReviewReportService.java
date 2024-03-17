package com.KociApp.RateGame.review.reports;

import com.KociApp.RateGame.review.Review;
import com.KociApp.RateGame.review.ReviewRepository;
import com.KociApp.RateGame.review.ReviewService;
import com.KociApp.RateGame.user.User;
import com.KociApp.RateGame.user.UserInfo.LoggedInUserProvider;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReviewReportService implements IReviewReportService{

    private final ReviewReportsRepository reviewReportsRepository;
    private final ReviewRepository reviewRepository;
    private final LoggedInUserProvider loggedInUserProvider;

    @Override
    public ReviewReport save(ReviewReportRequest reviewReportRequest) {

        ReviewReport reviewReport = new ReviewReport();

        //assigning the review
        Review review = reviewRepository.findById(reviewReportRequest.reviewId()).orElseThrow(
                () -> new EntityNotFoundException("Review with id-" + reviewReportRequest.reviewId() + " not found")
        );

        reviewReport.setReview(review);

        //assinging the reporting user
        User user = loggedInUserProvider.getLoggedUser();
        reviewReport.setUser(user);

        reviewReport.setContent(reviewReportRequest.content());

        //checking if this user already reported this review
        Optional<ReviewReport> reviewReportDb = reviewReportsRepository.findByUserAndReview(user, review);

        if(reviewReportDb.isPresent()){
            throw new EntityExistsException("User with id-" + user.getId() + " has already reported review with id-" + review.getId());
        }

        return reviewReportsRepository.save(reviewReport);
    }

    @Override
    public ReviewReport findById(Long id) {

        Optional<ReviewReport> reviewReport = reviewReportsRepository.findById(id);

        if(reviewReport.isEmpty()){
            throw new EntityNotFoundException("ReviewReport woth id- " + id + " not found");
        }

        return reviewReport.get();
    }

    @Override
    public List<ReviewReport> findAll() {
        return reviewReportsRepository.findAll();
    }

    @Override
    public int countByReview(Long id) {
        return reviewReportsRepository.countByReview_Id(id);
    }

    @Override
    public List<Long> findUniqueReviewIds() {
        return reviewReportsRepository.findUniqueReviewIds();
    }

    @Override
    public List<ReviewReportRaport> raport() {
        List<Long> reportedRewievsIds = findUniqueReviewIds();
        List<ReviewReportRaport> raport = new ArrayList<>();

        for(Long reviewId : reportedRewievsIds){

            Review review = reviewRepository.findById(reviewId).orElseThrow(
                    () -> new EntityNotFoundException("Review with id-" + reviewId + " not found")
            );

            raport.add(new ReviewReportRaport(reviewId, review.getContent(), countByReview(reviewId), review.getUser().getId()));
        }

        return raport;
    }

    @Override
    public String remove(Long id) {

        ReviewReport reviewReport = findById(id);
        reviewReportsRepository.delete(reviewReport);

        return "RewievRepor with id- " + id + " has been deleted";
    }
}
