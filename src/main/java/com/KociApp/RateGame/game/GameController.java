package com.KociApp.RateGame.game;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService service;

    @PostMapping
    public Game createGame(@RequestBody Game game){
        return service.save(game);
    }

    @GetMapping
    public List<Game> getGames(){
        return service.getGames();
    }

    @GetMapping("/byId/{id}")
    public Optional<Game> getGameById(@PathVariable int id){
        return service.findById(id);
    }

    @GetMapping("/byKeyWord/{keyWord}")
    public List<Game> getGamesByTitleLike(@PathVariable String keyWord){
        return service.findAllByTitleLike(keyWord);
    }

}
