package com.KociApp.RateGame.game.cover;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/covers")
public class CoverController {

    private final CoverService coverService;

    @GetMapping("/{gameId}")
    public Cover getCoverByGameId(@PathVariable int gameId){
        return coverService.findCoverByGameId(gameId);
    }
}
