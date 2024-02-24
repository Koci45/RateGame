package com.KociApp.RateGame.review;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByUserId(Long id);

    List<Review> findByGameId(int id);

    Optional<Review> findByUserIdAndGameId(Long userId, int gameId);

    @Query("SELECT r FROM Review r JOIN ReviewLike rl ON r.id = rl.review.id WHERE r.game.id = :gameId AND rl.likeDislike = true GROUP BY r.id ORDER BY COUNT(rl.id) DESC")
    List<Review> findTopLikedReviewsByGameId(int gameId, Pageable pageable);

}
