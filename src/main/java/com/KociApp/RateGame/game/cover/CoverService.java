package com.KociApp.RateGame.game.cover;

import com.KociApp.RateGame.game.Game;
import com.KociApp.RateGame.game.GameService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CoverService implements ICoverService{

    private final CoverRepository coverRepository;
    private final GameService gameService;
    @Override
    public Cover findCoverByGameId(int gameId) {
        Game game = gameService.findById(gameId);//just to check if that game exists, if not this function will trow aprioprtate exception

        Optional<Cover> cover = coverRepository.findByGame(gameId);

        if(cover.isEmpty()){
            throw new EntityNotFoundException("Cover for game with id: " + game.toString() + " doesnt exist");
        }

        return cover.get();
    }

}
