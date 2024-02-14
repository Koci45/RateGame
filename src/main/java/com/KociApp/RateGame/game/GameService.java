package com.KociApp.RateGame.game;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class GameService implements IGamesService{

    private final GameRepository repository;
    @Override
    public Optional<Game> findById(int id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Game> findByTitle(String title) {
        return repository.findByTitle(title);
    }

    @Override
    public List<Game> findAllByGenre(String genre) {
        return repository.findAllByGenre(genre);
    }

    @Override
    public List<Game> findAllByTitleLike(String keyWord) {
        return repository.findByTitleContaining(keyWord);
    }

    @Override
    public Game save(Game game) {
        return repository.save(game);
    }

    public List<Game> getGames() {
        return repository.findAll();
    }
}
