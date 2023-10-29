package com.example.demo.worldcup.repository;

import com.example.demo.domain.worldcup.model.WorldCupGame;
import com.example.demo.domain.worldcup.model.vo.VisibleType;
import com.example.demo.domain.worldcup.model.vo.WorldCupGameRound;
import com.example.demo.domain.worldcup.repository.WorldCupGameRepository;
import com.example.demo.helper.DataBaseCleanUp;
import com.example.demo.helper.web.config.TestWebConfig;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class WorldCupGameContentsRepositoryTest {

    @Autowired
    private WorldCupGameRepository worldCupGameRepository;

    @Autowired
    private DataBaseCleanUp dataBaseCleanUp;

    @BeforeEach
    public void setUp() {
        dataBaseCleanUp.truncateAllEntity();
    }

    @Test
    @DisplayName("WorldCupGameId와 일치하는 WorldCupGame이 존재")
    public void existsWorldCupGame_success() {
        WorldCupGame worldCupGame = createWorldCupGame(
                "TITLE",
                "DESCRIPTION",
                WorldCupGameRound.ROUND_32,
                VisibleType.PUBLIC,
                1
        );
        WorldCupGame savedWorldCupGame = worldCupGameRepository.save(worldCupGame);

        Boolean existsWorldCupGame = worldCupGameRepository.existsWorldCupGame(savedWorldCupGame.getId());

        assert existsWorldCupGame;
    }

    @Test
    @DisplayName("WorldCupGameId와 일치하는 WorldCupGame이 존재하지 않음")
    public void existsWorldCupGame_failed() {
        WorldCupGame worldCupGame = createWorldCupGame(
                "TITLE",
                "DESCRIPTION",
                WorldCupGameRound.ROUND_32,
                VisibleType.PUBLIC,
                1
        );
        WorldCupGame savedWorldCupGame = worldCupGameRepository.save(worldCupGame);
        Long plus1Id = savedWorldCupGame.getId() + 1;
        Boolean existsWorldCupGame = worldCupGameRepository.existsWorldCupGame(plus1Id);

        assert !existsWorldCupGame;
    }

    private WorldCupGame createWorldCupGame(
            String title,
            String description,
            WorldCupGameRound gameRound,
            VisibleType visibleType,
            int memberId)
    {
        return WorldCupGame.builder()
                .title(title)
                .description(description)
                .round(gameRound)
                .visibleType(visibleType)
                .views(0)
                .softDelete(false)
                .memberId(memberId)
                .build();
    }
}
