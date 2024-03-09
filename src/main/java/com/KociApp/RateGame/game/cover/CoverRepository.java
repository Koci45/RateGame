package com.KociApp.RateGame.game.cover;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CoverRepository extends JpaRepository<Cover, Integer> {

    @Query("SELECT MAX(c.game) FROM Cover c")
    Integer findHighestGameId();

    Optional<Cover> findByGame(int game);
}
