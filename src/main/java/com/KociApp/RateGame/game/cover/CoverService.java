package com.KociApp.RateGame.game.cover;

import com.KociApp.RateGame.game.Game;
import com.KociApp.RateGame.game.GameRepository;
import com.KociApp.RateGame.game.GameService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CoverService implements ICoverService{

    private final CoverRepository coverRepository;
    @Override
    public Cover findCoverByGameId(int gameId) {

        return coverRepository.findByGame(gameId).orElseThrow(
                () -> new EntityNotFoundException("game cover with gameId-" + gameId + " not found")
        );
    }

}
