package com.KociApp.RateGame.api.repositories;

import com.KociApp.RateGame.review.Review;
import com.KociApp.RateGame.review.reports.ReviewReport;
import com.KociApp.RateGame.review.reports.ReviewReportsRepository;
import com.KociApp.RateGame.user.User;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReviewReportsRepositoryTests {

    @Autowired
    private ReviewReportsRepository reviewReportsRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void findByUserAndReviewTest(){

        User user = new User();
        user.setId(1L);
        user.setEmail("test");
        entityManager.merge(user);

        Review reviewOne = new Review();
        reviewOne.setId(1L);
        reviewOne.setUser(user);
        reviewOne.setContent("test");
        entityManager.merge(reviewOne);

        Review reviewTwo = new Review();
        reviewTwo.setId(2L);
        entityManager.merge(reviewTwo);

        ReviewReport reviewReportOne = new ReviewReport();
        reviewReportOne.setId(1L);
        reviewReportOne.setReview(reviewOne);
        reviewReportOne.setUser(user);
        reviewReportsRepository.save(reviewReportOne);

        ReviewReport reviewReportTwo = new ReviewReport();
        reviewReportTwo.setId(2L);
        reviewReportTwo.setReview(reviewTwo);
        reviewReportTwo.setUser(user);
        reviewReportsRepository.save(reviewReportTwo);

        ReviewReport result = reviewReportsRepository.findByUserAndReview(user, reviewOne).get();

        Assertions.assertThat(result.getId()).isEqualTo(1L);
        Assertions.assertThat(result.getUser().getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(result.getReview().getContent()).isEqualTo(reviewOne.getContent());
    }

    @Test
    public void countByReview_IdTest(){

        User user = new User();
        user.setId(1L);
        user.setEmail("test");
        entityManager.merge(user);

        Review reviewOne = new Review();
        reviewOne.setId(1L);
        reviewOne.setUser(user);
        reviewOne.setContent("test");
        entityManager.merge(reviewOne);

        Review reviewTwo = new Review();
        reviewTwo.setId(2L);
        entityManager.merge(reviewTwo);

        ReviewReport reviewReportOne = new ReviewReport();
        reviewReportOne.setId(1L);
        reviewReportOne.setReview(reviewOne);
        reviewReportOne.setUser(user);
        reviewReportsRepository.save(reviewReportOne);

        ReviewReport reviewReportTwo = new ReviewReport();
        reviewReportTwo.setId(2L);
        reviewReportTwo.setReview(reviewOne);
        reviewReportTwo.setUser(user);
        reviewReportsRepository.save(reviewReportTwo);

        ReviewReport reviewReportThree = new ReviewReport();
        reviewReportThree.setId(3L);
        reviewReportThree.setReview(reviewTwo);
        reviewReportThree.setUser(user);
        reviewReportsRepository.save(reviewReportThree);

        int result = reviewReportsRepository.countByReview_Id(reviewOne.getId());

        Assertions.assertThat(result).isEqualTo(2);
    }

    @Test
    public void deleteAllByUser_IdTest(){

        User user = new User();
        user.setId(1L);
        user.setEmail("test");
        entityManager.merge(user);

        User userTwo = new User();
        userTwo.setId(2L);
        userTwo.setEmail("test2");
        entityManager.merge(userTwo);

        Review reviewOne = new Review();
        reviewOne.setId(1L);
        reviewOne.setUser(user);
        reviewOne.setContent("test");
        entityManager.merge(reviewOne);

        Review reviewTwo = new Review();
        reviewTwo.setId(2L);
        entityManager.merge(reviewTwo);

        ReviewReport reviewReportOne = new ReviewReport();
        reviewReportOne.setId(1L);
        reviewReportOne.setReview(reviewOne);
        reviewReportOne.setUser(user);
        reviewReportsRepository.save(reviewReportOne);

        ReviewReport reviewReportTwo = new ReviewReport();
        reviewReportTwo.setId(2L);
        reviewReportTwo.setReview(reviewOne);
        reviewReportTwo.setUser(user);
        reviewReportsRepository.save(reviewReportTwo);

        ReviewReport reviewReportThree = new ReviewReport();
        reviewReportThree.setId(3L);
        reviewReportThree.setReview(reviewTwo);
        reviewReportThree.setUser(userTwo);
        reviewReportsRepository.save(reviewReportThree);

        reviewReportsRepository.deleteAllByUser_Id(user.getId());

        List<ReviewReport> result = reviewReportsRepository.findAll();

        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void findUniqueReviewIds(){

        Review reviewOne = new Review();
        reviewOne.setId(1L);
        entityManager.merge(reviewOne);

        Review reviewTwo = new Review();
        reviewTwo.setId(2L);
        entityManager.merge(reviewTwo);

        Review reviewThree = new Review();
        reviewThree.setId(3L);
        entityManager.merge(reviewThree);

        ReviewReport reviewReportOne = new ReviewReport();
        reviewReportOne.setId(1L);
        reviewReportOne.setReview(reviewOne);
        reviewReportsRepository.save(reviewReportOne);

        ReviewReport reviewReportTwo = new ReviewReport();
        reviewReportTwo.setId(2L);
        reviewReportTwo.setReview(reviewOne);
        reviewReportsRepository.save(reviewReportTwo);

        ReviewReport reviewReportThree = new ReviewReport();
        reviewReportThree.setId(3L);
        reviewReportThree.setReview(reviewThree);
        reviewReportsRepository.save(reviewReportThree);

        List<Long> result = reviewReportsRepository.findUniqueReviewIds();

        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result.get(0)).isEqualTo(1L);
        Assertions.assertThat(result.get(1)).isEqualTo(3L);
    }
}
