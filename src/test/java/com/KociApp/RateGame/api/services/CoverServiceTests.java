package com.KociApp.RateGame.api.services;

import com.KociApp.RateGame.game.cover.Cover;
import com.KociApp.RateGame.game.cover.CoverRepository;
import com.KociApp.RateGame.game.cover.CoverService;
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
public class CoverServiceTests {

    @Mock
    private CoverRepository coverRepository;

    @InjectMocks
    private CoverService coverService;

    @Test
    public void findCoverByGameIdWithExistingId(){

        Cover cover = new Cover(1, 1, "//test");

        when(coverRepository.findByGame(1)).thenReturn(Optional.of(cover));

        Cover result = coverService.findCoverByGameId(1);

        Assertions.assertThat(result.getId()).isEqualTo(1);
        Assertions.assertThat(result.getGame()).isEqualTo(1);
    }

    @Test
    public void findCoverByGameIdWithNotExistingId(){

        Assertions.assertThatThrownBy(() -> coverService.findCoverByGameId(1)).isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("game cover with gameId-1 not found");
    }
}
