package com.example.demo.worldcup.service;

import com.example.demo.domain.worldcup.model.repository.WorldCupGameRepository;
import com.example.demo.domain.worldcup.service.WorldCupGameContentsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;
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

        Long worldCupGameId = 1L;

        worldCupGamecontentsService.getAvailableGameRounds(worldCupGameId);

        then(worldCupGameRepository)
                .should(times(1))
                .existsWorldGame(worldCupGameId);

        then(worldCupGameRepository)
                .should(times(1))
                .getAvailableGameRounds(worldCupGameId);

        then(worldCupGameRepository)
                .should(times(1))
                .incrementView(worldCupGameId);

    }
}
