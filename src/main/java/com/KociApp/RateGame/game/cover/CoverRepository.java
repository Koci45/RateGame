package com.KociApp.RateGame.game.cover;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CoverRepository extends JpaRepository<Cover, Integer> {

    @Query("SELECT MAX(c.game) FROM Cover c")
    Integer findHighestGameId();
}
