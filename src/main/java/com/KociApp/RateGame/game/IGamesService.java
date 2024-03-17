package com.KociApp.RateGame.game;

import java.util.List;

public interface IGamesService {

    Game findById(int id);

    List<Game> findAllByTitleLike(String keyWord);

    Game save(Game game);

}
