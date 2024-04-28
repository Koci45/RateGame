package com.KociApp.RateGame.game;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/games")
public class GameController {

    private final GameService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Game createGame(@RequestBody Game game){
        return service.save(game);
    }

    @GetMapping
    public List<Game> getGames(){
        return service.getGames();
    }

    @GetMapping("/byId/{id}")
    public Game getGameById(@PathVariable int id){
        return service.findById(id);
    }

    @GetMapping("/byKeyWord/{keyWord}")
    public List<Game> getGamesByTitleLike(@PathVariable String keyWord){
        return service.findAllByTitleLike(keyWord);
    }

    @GetMapping("InPages/{pageNumber}")
    public List<Game> getGamesInPages(@PathVariable int pageNumber){
        return service.getGamesInPages(PageRequest.of(pageNumber, 12));
    }

    @GetMapping("/byKeyWord/{keyWord}/{pageNumber}")
    public List<Game> getGamesInPages(@PathVariable String keyWord, @PathVariable int pageNumber){
        return service.findAllByTitleLikeInPages(keyWord, PageRequest.of(pageNumber, 12));
    }

}
