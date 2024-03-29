package com.KociApp.RateGame.api.services;

import com.KociApp.RateGame.game.Game;
import com.KociApp.RateGame.review.Review;
import com.KociApp.RateGame.review.ReviewRepository;
import com.KociApp.RateGame.review.reports.*;
import com.KociApp.RateGame.user.User;
import com.KociApp.RateGame.user.UserInfo.LoggedInUserProvider;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewReportServiceTests {

    @Mock
    private ReviewReportsRepository reviewReportsRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private LoggedInUserProvider loggedInUserProvider;

    @InjectMocks
    private ReviewReportService reviewReportService;

    @Test
    public void saveWithExistingReviewAndNotExistingReportForThatReviewByThatUserTest(){

        User user = new User(1L, "test", "test@test1", "123456", "USER", true);
        Review review = new Review(1L, "test", new Date(), user, new Game(), (byte) 50);
        ReviewReportRequest reviewReportRequest = new ReviewReportRequest("test", 1L);
        ReviewReport expectedReviewReport = new ReviewReport(1L, "test", user, review);

        when(reviewRepository.findById(reviewReportRequest.reviewId())).thenReturn(Optional.of(review));
        when(loggedInUserProvider.getLoggedUser()).thenReturn(user);
        when(reviewReportsRepository.save(any())).thenReturn(expectedReviewReport);

        ReviewReport result = reviewReportService.save(reviewReportRequest);

        ArgumentCaptor<ReviewReport> reviewReportCaptor = ArgumentCaptor.forClass(ReviewReport.class);
        verify(reviewReportsRepository, times(1)).save(reviewReportCaptor.capture());

        Assertions.assertThat(result.getReview()).isEqualTo(review);
        Assertions.assertThat(result.getUser()).isEqualTo(user);

        //asserting that this method assigns correct values to the fields its in charge of assigning
        Assertions.assertThat(reviewReportCaptor.getValue().getUser()).isEqualTo(user);
        Assertions.assertThat(reviewReportCaptor.getValue().getReview()).isEqualTo(review);
        Assertions.assertThat(reviewReportCaptor.getValue().getContent()).isEqualTo(reviewReportRequest.content());
    }

    @Test
    public void saveWithExistingReviewAndExistingReportForThatReviewByThatUserTest(){

        User user = new User(1L, "test", "test@test1", "123456", "USER", true);
        Review review = new Review(1L, "test", new Date(), user, new Game(), (byte) 50);
        ReviewReportRequest reviewReportRequest = new ReviewReportRequest("test", 1L);
        ReviewReport expectedReviewReport = new ReviewReport(1L, "test", user, review);

        when(reviewRepository.findById(reviewReportRequest.reviewId())).thenReturn(Optional.of(review));
        when(loggedInUserProvider.getLoggedUser()).thenReturn(user);
        when(reviewReportsRepository.findByUserAndReview(user, review)).thenReturn(Optional.of(expectedReviewReport));

        Assertions.assertThatThrownBy(() -> reviewReportService.save(reviewReportRequest)).isInstanceOf(EntityExistsException.class)
                .hasMessageContaining("User with id-" + user.getId() + " has already reported review with id-" + review.getId());
    }

    @Test
    public void saveWithNotExistingReviewAndNotExistingReportForThatReviewByThatUserTest() {

        ReviewReportRequest reviewReportRequest = new ReviewReportRequest("test", 1L);

        Assertions.assertThatThrownBy(() -> reviewReportService.save(reviewReportRequest)).isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Review with id-" + reviewReportRequest.reviewId() + " not found");
    }

    @Test
    public void findByIdWithExistingIdTest(){

        ReviewReport reviewReport = new ReviewReport(1L, "test", new User(), new Review());

        when(reviewReportsRepository.findById(1L)).thenReturn(Optional.of(reviewReport));

        ReviewReport result = reviewReportService.findById(1L);

        Assertions.assertThat(result.getId()).isEqualTo(reviewReport.getId());
        Assertions.assertThat(result.getContent()).isEqualTo(reviewReport.getContent());
        Assertions.assertThat(result.getReview()).isEqualTo(reviewReport.getReview());
        Assertions.assertThat(result.getUser()).isEqualTo(reviewReport.getUser());
    }

    @Test
    public void findByIdWithNotExistingIdTest(){

        Assertions.assertThatThrownBy(() -> reviewReportService.findById(1L)).isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("ReviewReport with id- 1 not found");
    }

    @Test
    public void findAllTest(){

        List<ReviewReport> reports = new ArrayList<>();
        reports.add(new ReviewReport(1L, "test", new User(), new Review()));
        reports.add(new ReviewReport(2L, "test", new User(), new Review()));

        when(reviewReportsRepository.findAll()).thenReturn(reports);

        List<ReviewReport> result = reviewReportService.findAll();

        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result.get(0).getId()).isEqualTo(1L);
        Assertions.assertThat(result.get(1).getId()).isEqualTo(2L);
    }

    @Test
    public void raportTestWithExistingReviewIds(){

        User user = new User(1L, "test", "test@test1", "123456", "USER", true);
        Review reviewOne = new Review(1L, "test1", new Date(), user, new Game(), (byte) 50);
        Review reviewTwo = new Review(2L, "test2", new Date(), user, new Game(), (byte) 50);
        Review reviewThree = new Review(3L, "test3", new Date(), user, new Game(), (byte) 50);

        List<Long> uniqueReviewIds = new ArrayList<>();
        uniqueReviewIds.add(1L);
        uniqueReviewIds.add(2L);
        uniqueReviewIds.add(3L);

        when(reviewReportsRepository.findUniqueReviewIds()).thenReturn(uniqueReviewIds);
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(reviewOne));
        when(reviewRepository.findById(2L)).thenReturn(Optional.of(reviewTwo));
        when(reviewRepository.findById(3L)).thenReturn(Optional.of(reviewThree));
        when(reviewReportsRepository.countByReview_Id(1L)).thenReturn(2);
        when(reviewReportsRepository.countByReview_Id(2L)).thenReturn(1);
        when(reviewReportsRepository.countByReview_Id(3L)).thenReturn(1);

        List<ReviewReportRaport> result = reviewReportService.raport();

        Assertions.assertThat(result.size()).isEqualTo(3);
        Assertions.assertThat(result.get(0).reviewReportsCount()).isEqualTo(2);
        Assertions.assertThat(result.get(1).reviewReportsCount()).isEqualTo(1);
        Assertions.assertThat(result.get(2).reviewReportsCount()).isEqualTo(1);
        Assertions.assertThat(result.get(0).reviewId()).isEqualTo(reviewOne.getId());
        Assertions.assertThat(result.get(1).reviewId()).isEqualTo(reviewTwo.getId());
        Assertions.assertThat(result.get(2).reviewId()).isEqualTo(reviewThree.getId());
    }

    @Test
    public void raportTestWithNotExistingReviewIds(){

        List<Long> uniqueReviewIds = new ArrayList<>();
        uniqueReviewIds.add(1L);

        when(reviewReportsRepository.findUniqueReviewIds()).thenReturn(uniqueReviewIds);
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> reviewReportService.raport()).isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Review with id-1 not found");

    }

    @Test
    public void removeWithExistingIdTest(){

        ReviewReport reviewReport = new ReviewReport(1L, "test", new User(), new Review());

        when(reviewReportsRepository.findById(1L)).thenReturn(Optional.of(reviewReport));

        String result = reviewReportService.remove(1L);

        verify(reviewReportsRepository, times(1)).delete(reviewReport);

        Assertions.assertThat(result).isEqualTo("RewievRepor with id- 1 has been deleted");
    }

    @Test
    public void removeWithNotExistingIdTest(){

        Assertions.assertThatThrownBy(() -> reviewReportService.remove(1L)).isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("ReviewReport with id- 1 not found");
    }

}
