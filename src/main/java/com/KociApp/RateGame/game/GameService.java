package com.KociApp.RateGame.game;

import com.KociApp.RateGame.game.cover.CoverRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class GameService implements IGamesService{

    private final GameRepository repository;
    @Override
    public Game findById(int id) {
        Optional<Game> game = repository.findById(id);

        if(game.isEmpty()){
            throw new EntityNotFoundException("game with id-" + id + " not found");
        }

        return game.get();
    }

    @Override
    public Game findByTitle(String title) {
        Optional<Game> game = repository.findByTitle(title);

        if(game.isEmpty()){
            throw new EntityNotFoundException("game with title-" + title + " not found");
        }

        return game.get();
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
