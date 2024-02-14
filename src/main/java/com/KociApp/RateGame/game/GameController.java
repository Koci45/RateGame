package com.KociApp.RateGame.game;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
