package com.KociApp.RateGame.game;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IGamesService {

    Game findById(int id);

    List<Game> findAllByTitleLike(String keyWord);

    Game save(Game game);

    List<Game> getGames();

    List<Game> getGamesInPages(PageRequest pageRequest);

    List<Game> findAllByTitleLikeInPages(String keyWord, PageRequest pageRequest);

}
