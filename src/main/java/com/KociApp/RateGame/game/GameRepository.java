package com.KociApp.RateGame.game;

import com.KociApp.RateGame.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface GameRepository extends JpaRepository<Game, Integer>, PagingAndSortingRepository<Game, Integer> {

    @Query("SELECT g FROM Game g WHERE g.title LIKE %:keyword%")
    List<Game> findByTitleContaining(@Param("keyword") String keyword);

    @Query("SELECT g FROM Game g WHERE g.title LIKE %:keyword%")
    Page<Game> findByTitleContaining(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT MAX(g.createdAt) FROM Game g")
    Date findLatestCreatedAt();

}
