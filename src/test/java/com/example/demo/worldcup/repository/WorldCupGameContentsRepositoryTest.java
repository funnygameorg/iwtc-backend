package com.example.demo.worldcup.repository;

import com.example.demo.TestConstant;
import com.example.demo.domain.etc.model.MediaFile;
import com.example.demo.domain.etc.repository.MediaFileRepository;
import com.example.demo.domain.worldcup.model.WorldCupGame;
import com.example.demo.domain.worldcup.model.WorldCupGameContents;
import com.example.demo.domain.worldcup.model.vo.VisibleType;
import com.example.demo.domain.worldcup.model.vo.WorldCupGameRound;
import com.example.demo.domain.worldcup.repository.WorldCupGameContentsRepository;
import com.example.demo.domain.worldcup.repository.WorldCupGameRepository;
import com.example.demo.domain.worldcup.repository.projection.GetAvailableGameRoundsProjection;
import com.example.demo.helper.AbstractContainerBaseTest;
import com.example.demo.helper.DataBaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static com.example.demo.TestConstant.SUCCESS_PREFIX;
import static com.example.demo.domain.worldcup.repository.impl.WorldCupGameContentsRepositoryImpl.WINNER_CONTENTS_SCORE_KEY_FORMAT;

@SpringBootTest
@ActiveProfiles("test")
public class WorldCupGameContentsRepositoryTest extends AbstractContainerBaseTest {

    @Autowired
    private WorldCupGameRepository worldCupGameRepository;
    @Autowired
    private WorldCupGameContentsRepository worldCupGameContentsRepository;
    @Autowired
    private MediaFileRepository mediaFileRepository;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DataBaseCleanUp dataBaseCleanUp;

    @AfterEach
    public void tearDown() {
        dataBaseCleanUp.truncateAllEntity();
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushDb();
    }

    @Nested
    @DisplayName("id를 사용해서 존재하는 월드컵 게임인지 찾을 수 있다.")
    public class existsWorldCupGame {
        @Test
        @DisplayName(SUCCESS_PREFIX + "월드컵 게임 존재 O")
        public void success() {
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
        @DisplayName(SUCCESS_PREFIX + "월드컵 게임 존재 X")
        public void success2() {
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

    }

    @Nested
    @DisplayName("월드컵 게임 1개에 대한 플레이 가능한 라운드 수 조회할 수 있다.")
    public class getAvailableGameRounds {
        @Test
        @DisplayName(SUCCESS_PREFIX + "'2'라운드 진행 가능, 컨텐츠 개수 3개")
        public void success1() {
            // given
            WorldCupGame worldCupGame = createWorldCupGame(
                    "TITLE",
                    "DESCRIPTION",
                    WorldCupGameRound.ROUND_32,
                    VisibleType.PUBLIC,
                    1
            );
            List<MediaFile> mediaFiles = IntStream.range(1,4)
                    .mapToObj(idx -> MediaFile.builder()
                            .originalName("original")
                            .filePath("filePath")
                            .absoluteName("absolute")
                            .extension(".png")
                            .build())
                    .toList();
            List<WorldCupGameContents> contentsList = IntStream.range(1,4)
                    .mapToObj(idx -> createGameContents(worldCupGame, "CONTENTS_NAME" + idx, mediaFiles.get(idx - 1)))
                    .toList();
            worldCupGameRepository.save(worldCupGame);
            mediaFileRepository.saveAll(mediaFiles);
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
        @DisplayName(SUCCESS_PREFIX + "'0'라운드 진행 가능, 컨텐츠 개수 0개")
        public void success2() {
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

    }

    @Nested
    @DisplayName("이상형 월드컵 게임의 순위권 컨텐츠의 점수 저장할 수 있다.")
    public class saveWinnerContentsScore {

        @Test
        @DisplayName(SUCCESS_PREFIX + "컨텐츠 1개 10점 저장")
        public void success1() {
            // given
            ValueOperations ops = redisTemplate.opsForValue();

            // when
            worldCupGameContentsRepository.saveWinnerContentsScore(1L, 1L, 10);

            // then
            String winnerPoint = (String) ops.get(WINNER_CONTENTS_SCORE_KEY_FORMAT.formatted(1L, 1L));
            assert Objects.equals(winnerPoint, "10");
        }

        @Test
        @DisplayName(SUCCESS_PREFIX + "동일 컨텐츠 21점 저장(10점 + 7점 + 4점 중첩)")
        public void success2() {
            // given
            ValueOperations ops = redisTemplate.opsForValue();

            // when
            worldCupGameContentsRepository.saveWinnerContentsScore(1L, 1L, 10);
            worldCupGameContentsRepository.saveWinnerContentsScore(1L, 1L, 7);
            worldCupGameContentsRepository.saveWinnerContentsScore(1L, 1L, 4);

            // then
            String winnerPoint = (String) ops.get(WINNER_CONTENTS_SCORE_KEY_FORMAT.formatted(1L, 1L));
            assert Objects.equals(winnerPoint, "21");
        }

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
                .visibleType(visibleType)
                .views(0)
                .softDelete(false)
                .memberId(memberId)
                .build();
    }

    private WorldCupGameContents createGameContents(
            WorldCupGame game,
            String name,
            MediaFile mediaFile
    ) {
        return WorldCupGameContents.builder()
                .name(name)
                .worldCupGame(game)
                .mediaFile(mediaFile)
                .build();
    }
}
