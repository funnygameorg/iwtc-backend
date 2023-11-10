package com.example.demo.worldcup.service;

import com.example.demo.domain.etc.model.MediaFile;
import com.example.demo.domain.etc.repository.MediaFileRepository;
import com.example.demo.domain.worldcup.controller.request.ClearWorldCupGameRequest;
import com.example.demo.domain.worldcup.controller.response.GetAvailableGameRoundsResponse;
import com.example.demo.domain.worldcup.controller.response.GetWorldCupPlayContentsResponse;
import com.example.demo.domain.worldcup.exception.IllegalWorldCupGameContentsException;
import com.example.demo.domain.worldcup.exception.NoRoundsAvailableToPlayException;
import com.example.demo.domain.worldcup.exception.NotFoundWorldCupGameException;
import com.example.demo.domain.worldcup.model.WorldCupGame;
import com.example.demo.domain.worldcup.model.WorldCupGameContents;
import com.example.demo.domain.worldcup.repository.WorldCupGameContentsRepository;
import com.example.demo.domain.worldcup.repository.WorldCupGameRepository;
import com.example.demo.domain.worldcup.repository.projection.GetDividedWorldCupGameContentsProjection;
import com.example.demo.domain.worldcup.service.WorldCupGameContentsService;
import com.example.demo.helper.testbase.ContainerBaseTest;
import com.example.demo.helper.DataBaseCleanUp;
import com.example.demo.helper.testbase.IntegrationBaseTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.Objects;

import static com.example.demo.helper.TestConstant.EXCEPTION_PREFIX;
import static com.example.demo.helper.TestConstant.SUCCESS_PREFIX;
import static com.example.demo.domain.worldcup.model.vo.VisibleType.*;
import static com.example.demo.domain.worldcup.repository.impl.WorldCupGameContentsRepositoryImpl.WINNER_CONTENTS_SCORE_KEY_FORMAT;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

public class WorldCupContentsServiceTest extends ContainerBaseTest implements IntegrationBaseTest {

    @Autowired
    private WorldCupGameContentsService worldCupGamecontentsService;
    @SpyBean
    private WorldCupGameRepository worldCupGameRepository;
    @Autowired
    private WorldCupGameContentsRepository worldCupGameContentsRepository;
    @Autowired
    private MediaFileRepository mediaFileRepository;
    @Autowired
    private DataBaseCleanUp dataBaseCleanUp;
    @Autowired
    private RedisTemplate redisTemplate;

    @AfterEach
    public void tearDown() {
        dataBaseCleanUp.truncateAllEntity();
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushDb();
    }


    @Nested
    @DisplayName("월드컵 게임 라운드 수를 조회할 수 있다.")
    public class getAvailableGameRounds {
        @Test
        @DisplayName(SUCCESS_PREFIX)
        public void success() {
            // given
            WorldCupGame worldCupGame = WorldCupGame
                    .builder()
                    .title("title1")
                    .description("description1")
                    .visibleType(PUBLIC)
                    .views(0)
                    .softDelete(false)
                    .memberId(1)
                    .build();

            List<MediaFile> mediaFiles = range(1, 10)
                    .mapToObj(idx ->
                            MediaFile.builder()
                                    .absoluteName("absolute")
                                    .extension(".png")
                                    .filePath("/abc")
                                    .originalName("origin")
                                    .build()
                    )
                    .toList();

            List<WorldCupGameContents> contentsList = range(1, 10)
                    .mapToObj(idx ->
                            WorldCupGameContents.builder()
                                    .name("contentsName")
                                    .worldCupGame(worldCupGame)
                                    .mediaFile(mediaFiles.get(idx - 1))
                                    .build()
                    )
                    .toList();
            worldCupGameRepository.save(worldCupGame);
            mediaFileRepository.saveAll(mediaFiles);
            worldCupGameContentsRepository.saveAll(contentsList);

            // when
            GetAvailableGameRoundsResponse result = worldCupGamecontentsService.getAvailableGameRounds(1L);

            // then
            assert result.worldCupId() == 1;
            assert Objects.equals(result.worldCupDescription(), "description1");
            assert result.rounds().equals(List.of(2, 4, 8));
        }

        @Test
        @DisplayName(EXCEPTION_PREFIX + "게임에 플레이 가능한 라운드가 존재하지 않음")
        public void fail1() {

            // given
            WorldCupGame worldCupGame = WorldCupGame
                    .builder()
                    .title("title1")
                    .description("description1")
                    .visibleType(PUBLIC)
                    .views(0)
                    .softDelete(false)
                    .memberId(1)
                    .build();
            worldCupGameRepository.save(worldCupGame);

            // when & then
            assertThrows(
                    NoRoundsAvailableToPlayException.class,
                    () -> worldCupGamecontentsService.getAvailableGameRounds(1L)
            );
        }

        @Test
        @DisplayName(EXCEPTION_PREFIX + "존재하지 않는 게임을 조회할 수 없음")
        public void fail2() {
            // when
            assertThrows(
                    NotFoundWorldCupGameException.class,
                    () -> worldCupGamecontentsService.getAvailableGameRounds(1L)
            );
        }

    }

    @Nested
    @DisplayName("이상형 월드컵 게임 플레이를 위한 컨텐츠를 조회할 수 있다.")
    public class getPlayContents {

        @Test
        @DisplayName(SUCCESS_PREFIX)
        public void success() {

            // given
            WorldCupGame worldCupGame = WorldCupGame
                    .builder()
                    .title("title1")
                    .description("description1")
                    .visibleType(PUBLIC)
                    .views(0)
                    .softDelete(false)
                    .memberId(1)
                    .build();

            List<MediaFile> mediaFiles = range(1, 10).mapToObj( idx ->
                    MediaFile.builder()
                            .originalName("fileOriginalName")
                            .absoluteName("fileAbsoluteName " + idx)
                            .filePath("/naver/.../ " + idx)
                            .extension(".png")
                            .build()
            ).toList();

            List<WorldCupGameContents> contentsList = range(1, 10).mapToObj( idx ->
                    WorldCupGameContents.builder()
                            .name("contentsName " + idx)
                            .worldCupGame(worldCupGame)
                            .mediaFile(mediaFiles.get(idx - 1))
                            .build()
            ).toList();

            worldCupGameRepository.save(worldCupGame);
            mediaFileRepository.saveAll(mediaFiles);
            worldCupGameContentsRepository.saveAll(contentsList);

            // when
            GetWorldCupPlayContentsResponse result = worldCupGamecontentsService.getPlayContents(
                    1L,
                    8,
                    1,
                    List.of()
            );

            // then
            assert result.worldCupId() == 1L;
            assert result.round() == 8;
            assert Objects.equals(result.title(), "title1");

            assert result.contentsList().size() == 8;

            assert result.contentsList().get(0).getContentsId() == 1;
            assert Objects.equals(result.contentsList().get(0).getName(), "contentsName 1");
            assert Objects.equals(result.contentsList().get(0).getAbsoluteName(), "fileAbsoluteName 1");
            assert Objects.equals(result.contentsList().get(0).getFilePath(), "/naver/.../ 1");

            assert result.contentsList().get(7).getContentsId() == 8;
            assert Objects.equals(result.contentsList().get(7).getName(), "contentsName 8");
            assert Objects.equals(result.contentsList().get(7).getAbsoluteName(), "fileAbsoluteName 8");
            assert Objects.equals(result.contentsList().get(7).getFilePath(), "/naver/.../ 8");


        }

        @Test
        @DisplayName(EXCEPTION_PREFIX + "예상한 컨텐츠 조회 사이즈와 실제 조회 사이즈가 다르면 안된다.")
        public void fail1() {

            // given
            WorldCupGame worldCupGame = WorldCupGame
                    .builder()
                    .title("title1")
                    .description("description1")
                    .visibleType(PUBLIC)
                    .views(0)
                    .softDelete(false)
                    .memberId(1)
                    .build();

            List<MediaFile> mediaFiles = range(1, 10).mapToObj(idx ->
                    MediaFile.builder()
                            .originalName("fileOriginalName")
                            .absoluteName("fileAbsoluteName")
                            .filePath("/naver/.../")
                            .extension(".png")
                            .build()
            ).toList();
            List<WorldCupGameContents> contentsList = range(1, 10).mapToObj(idx ->
                    WorldCupGameContents.builder()
                            .name("contentsName")
                            .worldCupGame(worldCupGame)
                            .mediaFile(mediaFiles.get(idx - 1))
                            .build()
            ).toList();

            worldCupGameRepository.save(worldCupGame);
            mediaFileRepository.saveAll(mediaFiles);
            worldCupGameContentsRepository.saveAll(contentsList);

            List<GetDividedWorldCupGameContentsProjection> ACTUAL_GET_CONTENTS_LIST = List.of(
                    new GetDividedWorldCupGameContentsProjection(1, "name", "absoulteName", "filePath"),
                    new GetDividedWorldCupGameContentsProjection(2, "name", "absoulteName", "filePath")
            );
            given(worldCupGameRepository.getDividedWorldCupGameContents(1L, 8, List.of()))
                    .willReturn(ACTUAL_GET_CONTENTS_LIST);


            // when & then
            IllegalWorldCupGameContentsException resultException =
                    assertThrows(
                            IllegalWorldCupGameContentsException.class,
                            () -> worldCupGamecontentsService.getPlayContents(
                                    1L,
                                    8,
                                    1,
                                    List.of()
                            )
                    );

            assert resultException.getPublicMessage().contains("조회 컨텐츠 수가 다름 ");
        }

        @Test
        @DisplayName(EXCEPTION_PREFIX + "이미 플레이한 게임 컨텐츠를 조회하면 안된다.")
        public void fail2() {

            // given
            WorldCupGame worldCupGame = WorldCupGame
                    .builder()
                    .title("title1")
                    .description("description1")
                    .visibleType(PUBLIC)
                    .views(0)
                    .softDelete(false)
                    .memberId(1)
                    .build();

            MediaFile mediaFile1 = MediaFile.builder()
                    .originalName("fileOriginalName")
                    .absoluteName("fileAbsoluteName")
                    .filePath("filePath")
                    .extension("extension")
                    .build();
            MediaFile mediaFile2 = MediaFile.builder()
                    .originalName("fileOriginalName")
                    .absoluteName("fileAbsoluteName")
                    .filePath("filePath")
                    .extension("extension")
                    .build();

            WorldCupGameContents contents1 = WorldCupGameContents.builder()
                    .name("contentsName")
                    .worldCupGame(worldCupGame)
                    .mediaFile(mediaFile1)
                    .build();
            WorldCupGameContents contents2 = WorldCupGameContents.builder()
                    .name("contentsName")
                    .worldCupGame(worldCupGame)
                    .mediaFile(mediaFile2)
                    .build();

            List<GetDividedWorldCupGameContentsProjection> ACTUAL_GET_CONTENTS_LIST = List.of(
                    new GetDividedWorldCupGameContentsProjection(1, "name", "absoulteName", "filePath"),
                    new GetDividedWorldCupGameContentsProjection(2, "name", "absoulteName", "filePath")
            );
            given(worldCupGameRepository.getDividedWorldCupGameContents(1L, 2, List.of(1L)))
                    .willReturn(ACTUAL_GET_CONTENTS_LIST);

            worldCupGameRepository.save(worldCupGame);
            mediaFileRepository.saveAll(List.of(mediaFile1, mediaFile2));
            worldCupGameContentsRepository.saveAll(List.of(contents1, contents2));


            // when & then
            final List<Long> ALREADY_PLAYED_CONTENTS_ID = List.of(1L);
            IllegalWorldCupGameContentsException resultException =
                    assertThrows(
                            IllegalWorldCupGameContentsException.class,
                            () -> worldCupGamecontentsService.getPlayContents(
                                    1L,
                                    2,
                                    1,
                                    ALREADY_PLAYED_CONTENTS_ID
                            )
                    );

            assert resultException.getPublicMessage().contains("컨텐츠 중복");
        }

    }



    @Test
    @DisplayName(SUCCESS_PREFIX + "게임을 클리어할 수 있다.")
    public void clearWorldCupGame() {
        ValueOperations ops = redisTemplate.opsForValue();
        // given
        WorldCupGame worldCupGame = WorldCupGame
                .builder()
                .title("title1")
                .description("description1")
                .visibleType(PUBLIC)
                .views(0)
                .softDelete(false)
                .memberId(1)
                .build();

        MediaFile mediaFile1 = MediaFile.builder()
                .originalName("fileOriginalName")
                .absoluteName("fileAbsoluteName")
                .filePath("filePath")
                .extension("extension")
                .build();
        MediaFile mediaFile2 = MediaFile.builder()
                .originalName("fileOriginalName")
                .absoluteName("fileAbsoluteName")
                .filePath("filePath")
                .extension("extension")
                .build();
        MediaFile mediaFile3 = MediaFile.builder()
                .originalName("fileOriginalName")
                .absoluteName("fileAbsoluteName")
                .filePath("filePath")
                .extension("extension")
                .build();
        MediaFile mediaFile4 = MediaFile.builder()
                .originalName("fileOriginalName")
                .absoluteName("fileAbsoluteName")
                .filePath("filePath")
                .extension("extension")
                .build();

        WorldCupGameContents contents1 = WorldCupGameContents.builder()
                .name("contentsName")
                .worldCupGame(worldCupGame)
                .mediaFile(mediaFile1)
                .build();
        WorldCupGameContents contents2 = WorldCupGameContents.builder()
                .name("contentsName")
                .worldCupGame(worldCupGame)
                .mediaFile(mediaFile2)
                .build();
        WorldCupGameContents contents3 = WorldCupGameContents.builder()
                .name("contentsName")
                .worldCupGame(worldCupGame)
                .mediaFile(mediaFile3)
                .build();
        WorldCupGameContents contents4 = WorldCupGameContents.builder()
                .name("contentsName")
                .worldCupGame(worldCupGame)
                .mediaFile(mediaFile4)
                .build();

        worldCupGameRepository.save(worldCupGame);
        mediaFileRepository.saveAll(List.of(mediaFile1, mediaFile2, mediaFile3, mediaFile4));
        worldCupGameContentsRepository.saveAll(List.of(contents1, contents2, contents3, contents4));

        ClearWorldCupGameRequest request = ClearWorldCupGameRequest.builder()
                .firstWinnerContentsId(1)
                .secondWinnerContentsId(2)
                .thirdWinnerContentsId(3)
                .fourthWinnerContentsId(4)
                .build();

        // when
        worldCupGamecontentsService.clearWorldCupGame(worldCupGame.getId(), request);

        // then
        String firstWinnerPoint = (String) ops.get(WINNER_CONTENTS_SCORE_KEY_FORMAT.formatted(1L, 1L));
        String secondWinnerPoint = (String) ops.get(WINNER_CONTENTS_SCORE_KEY_FORMAT.formatted(1L, 2L));
        String thirdWinnerPoint = (String) ops.get(WINNER_CONTENTS_SCORE_KEY_FORMAT.formatted(1L, 3L));
        String fourthWinnerPoint = (String) ops.get(WINNER_CONTENTS_SCORE_KEY_FORMAT.formatted(1L, 4L));

        assert Objects.equals(firstWinnerPoint, "10");
        assert Objects.equals(secondWinnerPoint, "7");
        assert Objects.equals(thirdWinnerPoint, "4");
        assert Objects.equals(fourthWinnerPoint, "4");
    }
}
