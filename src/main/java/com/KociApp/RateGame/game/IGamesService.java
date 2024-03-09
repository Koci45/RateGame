package com.KociApp.RateGame.game;

import java.util.List;
import java.util.Optional;

public interface IGamesService {

    Game findById(int id);
    Game findByTitle(String title);

    List<Game> findAllByTitleLike(String keyWord);

    Game save(Game game);

}
