package com.KociApp.RateGame.game;

import java.util.List;
import java.util.Optional;

public interface IGamesService {

    Optional<Game> findById(int id);
    Optional<Game> findByTitle(String title);
    List<Game> findAllByGenre(String genre);
    List<Game> findAllByTitleLike(String keyWord);

    Game save(Game game);

}
