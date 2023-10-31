package com.example.demo.worldcup.repository;

import com.example.demo.domain.worldcup.model.WorldCupGame;
import com.example.demo.domain.worldcup.model.WorldCupGameContents;
import com.example.demo.domain.worldcup.model.vo.VisibleType;
import com.example.demo.domain.worldcup.model.vo.WorldCupGameRound;
import com.example.demo.domain.worldcup.repository.WorldCupGameContentsRepository;
import com.example.demo.domain.worldcup.repository.WorldCupGameRepository;
import com.example.demo.domain.worldcup.repository.projection.GetAvailableGameRoundsProjection;
import com.example.demo.helper.DataBaseCleanUp;
import com.example.demo.helper.web.config.TestWebConfig;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@SpringBootTest
@ActiveProfiles("test")
public class WorldCupGameContentsRepositoryTest {

    @Autowired
    private WorldCupGameRepository worldCupGameRepository;
    @Autowired
    private WorldCupGameContentsRepository worldCupGameContentsRepository;

    @Autowired
    private DataBaseCleanUp dataBaseCleanUp;

    @AfterEach
    public void tearDown() {
        dataBaseCleanUp.truncateAllEntity();
    }

    @Test
    @DisplayName("WorldCupGameId와 일치하는 WorldCupGame이 존재")
    public void existsWorldCupGame_success() {
        // given
        WorldCupGame worldCupGame = createWorldCupGame(
                "TITLE",
                "DESCRIPTION",
                WorldCupGameRound.ROUND_32,
                VisibleType.PUBLIC,
                1
        );
        WorldCupGame savedWorldCupGame = worldCupGameRepository.save(worldCupGame);

        // when
        Boolean existsWorldCupGame = worldCupGameRepository.existsWorldCupGame(savedWorldCupGame.getId());

        // then
        assert existsWorldCupGame;
    }

    @Test
    @DisplayName("WorldCupGameId와 일치하는 WorldCupGame이 존재하지 않음")
    public void existsWorldCupGame_failed() {
        // given
        WorldCupGame worldCupGame = createWorldCupGame(
                "TITLE",
                "DESCRIPTION",
                WorldCupGameRound.ROUND_32,
                VisibleType.PUBLIC,
                1
        );
        WorldCupGame savedWorldCupGame = worldCupGameRepository.save(worldCupGame);
        Long plus1Id = savedWorldCupGame.getId() + 1;

        // when
        Boolean existsWorldCupGame = worldCupGameRepository.existsWorldCupGame(plus1Id);

        // then
        assert !existsWorldCupGame;
    }

    @Test
    @DisplayName("월드컵 게임 1개에 대한 플레이 가능한 라운드 수 조회 - 컨텐츠 개수 3개")
    public void getAvailableGameRounds_contents_size_3() {
        // given
        WorldCupGame worldCupGame = createWorldCupGame(
                "TITLE",
                "DESCRIPTION",
                WorldCupGameRound.ROUND_32,
                VisibleType.PUBLIC,
                1
        );
        List<WorldCupGameContents> contentsList = IntStream.range(1,4)
                .mapToObj(idx -> createGameContents(worldCupGame, "CONTENTS_NAME" + idx, idx))
                .toList();
        worldCupGameRepository.save(worldCupGame);
        worldCupGameContentsRepository.saveAll(contentsList);

        // when
        GetAvailableGameRoundsProjection result = worldCupGameRepository.getAvailableGameRounds(worldCupGame.getId());

        // then
        assert Objects.equals(worldCupGame.getId(), result.worldCupId());
        assert Objects.equals(worldCupGame.getTitle(), result.worldCupTitle());
        assert Objects.equals(worldCupGame.getDescription(), result.worldCupDescription());
        assert result.totalContentsSize() == 3;
    }

    @Test
    @DisplayName("월드컵 게임 1개에 대한 플레이 가능한 라운드 수 조회 - 컨텐츠 개수 0개")
    public void getAvailableGameRounds_contents_size_0() {
        // given
        WorldCupGame worldCupGame = createWorldCupGame(
                "TITLE",
                "DESCRIPTION",
                WorldCupGameRound.ROUND_32,
                VisibleType.PUBLIC,
                1
        );

        WorldCupGame savedWorldCupGame = worldCupGameRepository.save(worldCupGame);

        Long worldCupGameId = savedWorldCupGame.getId();

        // when
        GetAvailableGameRoundsProjection result = worldCupGameRepository.getAvailableGameRounds(worldCupGameId);

        // then
        assert Objects.equals(worldCupGame.getId(), result.worldCupId());
        assert Objects.equals(worldCupGame.getTitle(), result.worldCupTitle());
        assert Objects.equals(worldCupGame.getDescription(), result.worldCupDescription());
        assert result.totalContentsSize() == 0;
    }

    private WorldCupGame createWorldCupGame(
            String title,
            String description,
            WorldCupGameRound gameRound,
            VisibleType visibleType,
            int memberId
    ) {
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

    private WorldCupGameContents createGameContents(
            WorldCupGame game,
            String name,
            int mediaFileId
    ) {
        return WorldCupGameContents.builder()
                .name(name)
                .worldCupGame(game)
                .mediaFileId(mediaFileId)
                .build();
    }
}
