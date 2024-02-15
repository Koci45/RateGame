package com.KociApp.RateGame.game;

import com.KociApp.RateGame.exception.game.GameNotFoundException;
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

        Optional<Game> game = repository.findById(id);

        if(!game.isPresent()){
            throw new GameNotFoundException("game with id-" + id + " not found");
        }

        return game;
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
