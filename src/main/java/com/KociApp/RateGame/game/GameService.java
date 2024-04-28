package com.KociApp.RateGame.game;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
    public List<Game> findAllByTitleLike(String keyWord) {
        return repository.findByTitleContaining(keyWord);
    }

    @Override
    public Game save(Game game) {
        return repository.save(game);
    }

    @Override
    public List<Game> getGames() {
        return repository.findAll();
    }

    @Override
    public List<Game> getGamesInPages(PageRequest pageRequest){
        return repository.findAll(pageRequest).stream().toList();
    }

    @Override
    public List<Game> findAllByTitleLikeInPages(String keyWord, PageRequest pageRequest) {
        return repository.findByTitleContaining(keyWord, pageRequest).stream().toList();
    }
}
