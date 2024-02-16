package com.masikga.itwc.domain.worldcup.repository;

import com.masikga.itwc.domain.etc.model.StaticMediaFile;
import com.masikga.itwc.domain.etc.repository.MediaFileRepository;
import com.masikga.itwc.domain.worldcup.model.WorldCupGame;
import com.masikga.itwc.domain.worldcup.model.WorldCupGameContents;
import com.masikga.itwc.domain.worldcup.model.vo.VisibleType;
import com.masikga.itwc.domain.worldcup.model.vo.WorldCupGameRound;
import com.masikga.itwc.domain.worldcup.repository.projection.GetAvailableGameRoundsProjection;
import com.masikga.itwc.helper.DataBaseCleanUp;
import com.masikga.itwc.helper.testbase.ContainerBaseTest;
import com.masikga.itwc.helper.testbase.IntegrationBaseTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.stream.IntStream;

import static com.masikga.itwc.domain.worldcup.model.vo.VisibleType.PUBLIC;
import static com.masikga.itwc.helper.TestConstant.SUCCESS_PREFIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class WorldCupGameContentsRepositoryTest extends ContainerBaseTest implements IntegrationBaseTest {

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
        @DisplayName(SUCCESS_PREFIX)
        public void success() {
            // given
            WorldCupGame worldCupGame = createWorldCupGame(
                    "TITLE",
                    "DESCRIPTION",
                    WorldCupGameRound.ROUND_32,
                    PUBLIC,
                    1
            );
            WorldCupGame savedWorldCupGame = worldCupGameRepository.save(worldCupGame);

            // when
            Boolean existsWorldCupGame = worldCupGameRepository.existsWorldCupGame(savedWorldCupGame.getId());

            // then
            assertThat(existsWorldCupGame).isTrue();
        }

        @Test
        @DisplayName(SUCCESS_PREFIX + "월드컵 게임 존재 X")
        public void success2() {
            // given
            WorldCupGame worldCupGame = createWorldCupGame(
                    "TITLE",
                    "DESCRIPTION",
                    WorldCupGameRound.ROUND_32,
                    PUBLIC,
                    1
            );
            WorldCupGame savedWorldCupGame = worldCupGameRepository.save(worldCupGame);
            Long plus1Id = savedWorldCupGame.getId() + 1;

            // when
            Boolean existsWorldCupGame = worldCupGameRepository.existsWorldCupGame(plus1Id);

            // then
            assertThat(existsWorldCupGame).isFalse();
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
                    PUBLIC,
                    1
            );
            List<StaticMediaFile> mediaFiles = IntStream.range(1, 4)
                    .mapToObj(idx -> StaticMediaFile.builder()
                            .originalName("original")
                            .extension("MP4")
                            .objectKey("filePath")
                            .originalFileSize("0")
                            .build())
                    .toList();
            List<WorldCupGameContents> contentsList = IntStream.range(1, 4)
                    .mapToObj(idx -> createGameContents(worldCupGame, "CONTENTS_NAME" + idx, mediaFiles.get(idx - 1)))
                    .toList();
            worldCupGameRepository.save(worldCupGame);
            mediaFileRepository.saveAll(mediaFiles);
            worldCupGameContentsRepository.saveAll(contentsList);

            // when
            GetAvailableGameRoundsProjection result = worldCupGameRepository.getAvailableGameRoundsV2(
                    worldCupGame.getId());

            // then
            assertAll(
                    () -> assertThat(worldCupGame.getId()).isEqualTo(result.worldCupId()),
                    () -> assertThat(worldCupGame.getTitle()).isEqualTo(result.worldCupTitle()),
                    () -> assertThat(worldCupGame.getDescription()).isEqualTo(result.worldCupDescription()),
                    () -> assertThat(result.totalContentsSize()).isEqualTo(3)
            );
        }

        @Test
        @DisplayName(SUCCESS_PREFIX + "'0'라운드 진행 가능, 컨텐츠 개수 0개")
        public void success2() {
            // given
            WorldCupGame worldCupGame = createWorldCupGame(
                    "TITLE",
                    "DESCRIPTION",
                    WorldCupGameRound.ROUND_32,
                    PUBLIC,
                    1
            );

            WorldCupGame savedWorldCupGame = worldCupGameRepository.save(worldCupGame);

            Long worldCupGameId = savedWorldCupGame.getId();

            // when
            GetAvailableGameRoundsProjection result = worldCupGameRepository.getAvailableGameRoundsV2(worldCupGameId);

            // then
            assertAll(
                    () -> assertThat(worldCupGame.getId()).isEqualTo(result.worldCupId()),
                    () -> assertThat(worldCupGame.getTitle()).isEqualTo(result.worldCupTitle()),
                    () -> assertThat(worldCupGame.getDescription()).isEqualTo(result.worldCupDescription()),
                    () -> assertThat(result.totalContentsSize()).isEqualTo(0)
            );
        }

    }

    @Nested
    @DisplayName("이상형 월드컵 게임의 순위권 컨텐츠의 점수 저장할 수 있다.")
    public class saveWinnerContentsScore {

        @Test
        @DisplayName(SUCCESS_PREFIX + "컨텐츠 1개 10점 저장")
        public void success1() {
            // given
            var worldCupGame = WorldCupGame.builder()
                    .title("title1")
                    .visibleType(PUBLIC)
                    .build();

            var mediaFile = StaticMediaFile.builder()
                    .objectKey("testObjectKey")
                    .originalName("originalName")
                    .extension("GIF")
                    .originalFileSize("0")
                    .build();

            var gameContents = WorldCupGameContents.builder()
                    .worldCupGame(worldCupGame)
                    .mediaFile(mediaFile)
                    .name("name")
                    .build();

            worldCupGameRepository.save(worldCupGame);
            mediaFileRepository.save(mediaFile);
            worldCupGameContentsRepository.save(gameContents);

            // when
            worldCupGameContentsRepository.saveWinnerContentsScore(1L, 1L, 10);

            var winnerPoint = worldCupGameContentsRepository.findById(1L).get().getGameScore();
            // then
            assertThat(winnerPoint).isEqualTo(10);
        }

        @Test
        @DisplayName(SUCCESS_PREFIX + "동일 컨텐츠 21점 저장(10점 + 7점 + 4점 중첩)")
        public void success2() {
            // given
            var worldCupGame = WorldCupGame.builder()
                    .title("title1")
                    .visibleType(PUBLIC)
                    .build();

            var mediaFile = StaticMediaFile.builder()
                    .objectKey("testObjectKey")
                    .originalName("originalName")
                    .extension("JPEG")
                    .originalFileSize("0")
                    .build();

            var gameContents = WorldCupGameContents.builder()
                    .worldCupGame(worldCupGame)
                    .mediaFile(mediaFile)
                    .name("name")
                    .build();

            worldCupGameRepository.save(worldCupGame);
            mediaFileRepository.save(mediaFile);
            worldCupGameContentsRepository.save(gameContents);

            // when
            worldCupGameContentsRepository.saveWinnerContentsScore(1L, 1L, 10);
            worldCupGameContentsRepository.saveWinnerContentsScore(1L, 1L, 7);
            worldCupGameContentsRepository.saveWinnerContentsScore(1L, 1L, 4);

            var winnerPoint = worldCupGameContentsRepository.findById(1L).get().getGameScore();
            // then
            assertThat(winnerPoint).isEqualTo(21);
        }
    }

    @Nested
    @DisplayName("월드컵 게임에 포함된 모든 컨텐츠를 조회할 수 있다.")
    class FindAllByWorldCupGame {

        @Test
        @DisplayName(SUCCESS_PREFIX)
        public void findAllByWorldCupGame() {
            WorldCupGame worldCupGame = createWorldCupGame(
                    "TITLE",
                    "DESCRIPTION",
                    WorldCupGameRound.ROUND_32,
                    PUBLIC,
                    1
            );
            List<StaticMediaFile> mediaFiles = IntStream.range(1, 4)
                    .mapToObj(idx -> StaticMediaFile.builder()
                            .originalName("original")
                            .objectKey("filePath")
                            .originalFileSize("0")
                            .extension("PNG")
                            .build())
                    .toList();
            List<WorldCupGameContents> contentsList = IntStream.range(1, 4)
                    .mapToObj(idx -> createGameContents(worldCupGame, "CONTENTS_NAME" + idx, mediaFiles.get(idx - 1)))
                    .toList();
            worldCupGameRepository.save(worldCupGame);
            mediaFileRepository.saveAll(mediaFiles);
            worldCupGameContentsRepository.saveAll(contentsList);

            // when
            List<WorldCupGameContents> result = worldCupGameContentsRepository.findAllByWorldCupGame(worldCupGame);

            // then
            assertAll(
                    () -> assertThat(result.size()).isEqualTo(3),

                    () -> assertThat(result.get(0).getId()).isEqualTo(1L),
                    () -> assertThat(result.get(0).getMediaFile().getId()).isEqualTo(1L),

                    () -> assertThat(result.get(1).getId()).isEqualTo(2L),
                    () -> assertThat(result.get(1).getMediaFile().getId()).isEqualTo(2L),

                    () -> assertThat(result.get(2).getId()).isEqualTo(3L),
                    () -> assertThat(result.get(2).getMediaFile().getId()).isEqualTo(3L)
            );
        }

        @Test
        @DisplayName(SUCCESS_PREFIX + "모든 컨텐츠의 상태가 softDeleted 상태, 결과 0건")
        public void success2() {
            WorldCupGame worldCupGame = createWorldCupGame(
                    "TITLE",
                    "DESCRIPTION",
                    WorldCupGameRound.ROUND_32,
                    PUBLIC,
                    1
            );
            List<StaticMediaFile> mediaFiles = IntStream.range(1, 4)
                    .mapToObj(idx -> StaticMediaFile.builder()
                            .originalName("original")
                            .objectKey("filePath")
                            .extension("PNG")
                            .originalFileSize("0")
                            .build())
                    .toList();
            List<WorldCupGameContents> contentsList = IntStream.range(1, 4)
                    .mapToObj(idx ->
                            WorldCupGameContents.builder()
                                    .name("CONTENTS_NAME" + idx)
                                    .mediaFile(mediaFiles.get(idx - 1))
                                    .worldCupGame(worldCupGame)
                                    .softDelete(true)
                                    .build()
                    )
                    .toList();
            worldCupGameRepository.save(worldCupGame);
            mediaFileRepository.saveAll(mediaFiles);
            worldCupGameContentsRepository.saveAll(contentsList);

            // when
            List<WorldCupGameContents> result = worldCupGameContentsRepository.findAllByWorldCupGame(worldCupGame);

            // then
            assertThat(result.size()).isEqualTo(0);

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
            StaticMediaFile mediaFile
    ) {
        return WorldCupGameContents.builder()
                .name(name)
                .worldCupGame(game)
                .mediaFile(mediaFile)
                .build();
    }
}
