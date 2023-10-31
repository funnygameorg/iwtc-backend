package com.example.demo.worldcup.service;

import com.example.demo.domain.worldcup.controller.response.GetWorldCupPlayContentsResponse;
import com.example.demo.domain.worldcup.exception.IllegalWorldCupGameContentsException;
import com.example.demo.domain.worldcup.exception.NoRoundsAvailableToPlayException;
import com.example.demo.domain.worldcup.exception.NotFoundWorldCupGameException;
import com.example.demo.domain.worldcup.model.WorldCupGame;
import com.example.demo.domain.worldcup.model.WorldCupGameContents;
import com.example.demo.domain.worldcup.model.vo.WorldCupGameRound;
import com.example.demo.domain.worldcup.repository.WorldCupGameRepository;
import com.example.demo.domain.worldcup.repository.projection.GetAvailableGameRoundsProjection;
import com.example.demo.domain.worldcup.repository.projection.GetDividedWorldCupGameContentsProjection;
import com.example.demo.domain.worldcup.service.WorldCupGameContentsService;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.zip.GZIPInputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

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
                .incrementWorldCupGameViews(worldCupGameId);
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
                .incrementWorldCupGameViews(worldCupGameId);
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
                .incrementWorldCupGameViews(worldCupGameId);
    }


    @Test
    @DisplayName("이상형 월드컵 게임 플레이를 위한 컨텐츠 조회 - 예상한 컨텐츠 조회 크기와 다르다. 예외 처리")
    public void GetPlayContents2() {
        // given
        Long worldCupId = 1L;
        int currentRound = 128;
        int divideContentsSizePerRequest = 4;
        List<Long> alreadyPlayedContentsIds = List.of();

        List mockList = mock(List.class);
        WorldCupGame mockWorldCoupGame = mock(WorldCupGame.class);
        given(mockList.size())
                .willReturn(divideContentsSizePerRequest + 1);

        given(worldCupGameRepository.findById(worldCupId))
                .willReturn(Optional.of(mockWorldCoupGame));


        given(worldCupGameRepository
                .getDividedWorldCupGameContents(
                        any(),
                        anyInt(),
                        any()
                )
        )
                .willReturn(mockList);


        // when & then
        IllegalWorldCupGameContentsException resultException = assertThrows(
                IllegalWorldCupGameContentsException.class,
                () -> worldCupGamecontentsService.getPlayContents(
                        worldCupId,
                        currentRound,
                        divideContentsSizePerRequest,
                        alreadyPlayedContentsIds
                )
        );
        assert resultException.getPublicMessage().contains("조회 컨텐츠 수가 다름 ");
        then(worldCupGameRepository)
                .should(times(1))
                .getDividedWorldCupGameContents(
                        anyLong(),
                        anyInt(),
                        anyList()
                );
    }

    @Test
    @DisplayName("이상형 월드컵 게임 플레이를 위한 컨텐츠 조회 - 이미 플레이한 게임 컨텐츠를 조회, 예외 처리")
    public void GetPlayContents3() {
        // given
        Long worldCupId = 1L;
        int currentRound = 4;
        int divideContentsSizePerRequest = 1;
        List<Long> alreadyPlayedContentsIds = List.of(3L);

        WorldCupGame mockWorldCoupGame = mock(WorldCupGame.class);

        List<GetDividedWorldCupGameContentsProjection> projections = IntStream.range(1,5)
                        .mapToObj(idx ->
                                new GetDividedWorldCupGameContentsProjection(
                                        idx,
                                        "TEST",
                                        "TEST",
                                        "TEST"
                                )
                        ).toList();

        given(worldCupGameRepository.findById(worldCupId))
                .willReturn(Optional.of(mockWorldCoupGame));


        given(worldCupGameRepository
                .getDividedWorldCupGameContents(
                        anyLong(),
                        anyInt(),
                        anyList()
                )
        )
                .willReturn(projections);


        // when & then
        IllegalWorldCupGameContentsException resultException = assertThrows(
                IllegalWorldCupGameContentsException.class,
                () -> worldCupGamecontentsService.getPlayContents(
                        worldCupId,
                        currentRound,
                        divideContentsSizePerRequest,
                        alreadyPlayedContentsIds
                )
        );
        
        assert resultException.getPublicMessage().contains("컨텐츠 중복");

        then(worldCupGameRepository)
                .should(times(1))
                .getDividedWorldCupGameContents(
                        anyLong(),
                        anyInt(),
                        anyList()
                );
    }
}
