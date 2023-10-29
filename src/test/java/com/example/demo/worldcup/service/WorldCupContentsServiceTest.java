package com.example.demo.worldcup.service;

import com.example.demo.domain.worldcup.exception.NoRoundsAvailableToPlayException;
import com.example.demo.domain.worldcup.exception.NotFoundWorldCupGameException;
import com.example.demo.domain.worldcup.repository.WorldCupGameRepository;
import com.example.demo.domain.worldcup.repository.projection.GetAvailableGameRoundsProjection;
import com.example.demo.domain.worldcup.service.WorldCupGameContentsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class WorldCupContentsServiceTest {

    @InjectMocks
    private WorldCupGameContentsService worldCupGamecontentsService;
    @Mock
    private WorldCupGameRepository worldCupGameRepository;


    @Test
    @DisplayName("플레이 가능한 라운드가 존재하는 게임 - 성공")
    public void 플레이_가능한_라운드가_존재하는_게임_성공() {

        // given
        Long worldCupGameId = 1L;
        given(worldCupGameRepository.existsWorldCupGame(worldCupGameId))
                .willReturn(true);
        given(worldCupGameRepository.getAvailableGameRounds(worldCupGameId))
                .willReturn(
                        new GetAvailableGameRoundsProjection(
                                1L,
                                "TEST_TITLE",
                                "TEST_DESC",
                                3L
                        )
                );
        // when
        worldCupGamecontentsService.getAvailableGameRounds(worldCupGameId);

        // then
        then(worldCupGameRepository)
                .should(times(1))
                .existsWorldCupGame(worldCupGameId);

        then(worldCupGameRepository)
                .should(times(1))
                .getAvailableGameRounds(worldCupGameId);

        then(worldCupGameRepository)
                .should(times(1))
                .incrementView(worldCupGameId);
    }

    @Test
    @DisplayName("플레이 가능한 라운드가 존재하지 않는 게임")
    public void 플레이_가능한_라운드가_존재하지_않는_게임() {

        // given
        Long worldCupGameId = 1L;
        given(worldCupGameRepository.existsWorldCupGame(worldCupGameId))
                .willReturn(true);
        given(worldCupGameRepository.getAvailableGameRounds(worldCupGameId))
                .willReturn(
                        new GetAvailableGameRoundsProjection(
                                1L,
                                "TEST_TITLE",
                                "TEST_DESC",
                                1L
                        )
                );
        // when
        assertThrows(
                NoRoundsAvailableToPlayException.class,
                () -> worldCupGamecontentsService.getAvailableGameRounds(worldCupGameId)
        );

        // then
        then(worldCupGameRepository)
                .should(times(1))
                .existsWorldCupGame(worldCupGameId);

        then(worldCupGameRepository)
                .should(times(1))
                .getAvailableGameRounds(worldCupGameId);

        then(worldCupGameRepository)
                .should(never())
                .incrementView(worldCupGameId);
    }
    @Test
    @DisplayName("존재하지 않는 게임의 라운드 수 조회")
    public void 존재하지_않는_게임의_라운드_수_조회() {

        // given
        Long worldCupGameId = 1L;
        given(worldCupGameRepository.existsWorldCupGame(worldCupGameId))
                .willReturn(false);
        // when
        assertThrows(
                NotFoundWorldCupGameException.class,
                () -> worldCupGamecontentsService.getAvailableGameRounds(worldCupGameId)
        );

        // then
        then(worldCupGameRepository)
                .should(times(1))
                .existsWorldCupGame(worldCupGameId);

        then(worldCupGameRepository)
                .should(never())
                .getAvailableGameRounds(worldCupGameId);

        then(worldCupGameRepository)
                .should(never())
                .incrementView(worldCupGameId);
    }
}
