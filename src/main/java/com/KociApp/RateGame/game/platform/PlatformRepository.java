package com.KociApp.RateGame.game.platform;

import com.KociApp.RateGame.game.genre.GenreRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlatformRepository extends JpaRepository<Platform, Integer> {

    Optional<Platform> findById(int id);
}
