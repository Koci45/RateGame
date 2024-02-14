package com.KociApp.RateGame.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Integer> {

    Optional<Game> findByTitle(String title);

    List<Game> findAllByGenre(String genre);

    @Query("SELECT g FROM Game g WHERE g.title LIKE %:keyword%")
    List<Game> findByTitleContaining(@Param("keyword") String keyword);
}
