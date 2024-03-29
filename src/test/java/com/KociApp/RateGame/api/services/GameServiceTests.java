package com.KociApp.RateGame.api.services;

import com.KociApp.RateGame.game.Game;
import com.KociApp.RateGame.game.GameRepository;
import com.KociApp.RateGame.game.GameService;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameServiceTests {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    @Test
    public void findByIdWithExistingIdTest(){

        Game game = new Game();
        game.setId(1);

        when(gameRepository.findById(1)).thenReturn(Optional.of(game));

        Game result = gameService.findById(1);

        Assertions.assertThat(result.getId()).isEqualTo(1);
    }

    @Test
    public void findByIdWithNotExistingIdTest(){

        Assertions.assertThatThrownBy(() -> gameService.findById(1)).isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("game with id-1 not found");
    }
}
