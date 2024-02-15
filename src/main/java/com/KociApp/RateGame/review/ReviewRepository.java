package com.KociApp.RateGame.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByUserId(Long id);

    List<Review> findByGameId(int id);

    Optional<Review> findByUserIdAndGameId(Long userId, int gameId);

}
