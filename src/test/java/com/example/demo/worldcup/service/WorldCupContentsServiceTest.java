package com.example.demo.worldcup.service;

import com.example.demo.domain.etc.model.InternetVideoUrl;
import com.example.demo.domain.etc.model.StaticMediaFile;
import com.example.demo.domain.etc.repository.AbstractMediaFileRepository;
import com.example.demo.domain.etc.repository.MediaFileRepository;
import com.example.demo.domain.worldcup.controller.request.ClearWorldCupGameRequest;
import com.example.demo.domain.worldcup.controller.response.ClearWorldCupGameResponse;
import com.example.demo.domain.worldcup.controller.response.GetAvailableGameRoundsResponse;
import com.example.demo.domain.worldcup.controller.response.GetWorldCupPlayContentsResponse;
import com.example.demo.domain.worldcup.exception.*;
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

import static com.example.demo.helper.TestConstant.EXCEPTION_PREFIX;
import static com.example.demo.helper.TestConstant.SUCCESS_PREFIX;
import static com.example.demo.domain.worldcup.model.vo.VisibleType.*;
import static com.example.demo.domain.worldcup.repository.impl.WorldCupGameContentsRepositoryImpl.WINNER_CONTENTS_SCORE_KEY_FORMAT;
import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
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
    private AbstractMediaFileRepository abstractMediaFileRepository;
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

            List<StaticMediaFile> mediaFiles = range(1, 10)
                    .mapToObj(idx ->
                            StaticMediaFile.builder()
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
            assertAll(
                    () -> assertThat(result.worldCupId()).isEqualTo(1),
                    () -> assertThat(result.worldCupDescription()).isEqualTo("description1"),
                    () -> assertThat(result.rounds()).isEqualTo(List.of(2, 4, 8))
            );
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
        @DisplayName(SUCCESS_PREFIX + "8개 조회 (정적 파일 10개)")
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

            List<StaticMediaFile> mediaFiles = range(1, 10).mapToObj(idx ->
                    StaticMediaFile.builder()
                            .originalName("fileOriginalName")
                            .absoluteName("fileAbsoluteName" + idx)
                            .filePath("/naver/.../" + idx)
                            .extension(".png")
                            .build()
            ).toList();

            List<WorldCupGameContents> contentsList = range(1, 10).mapToObj( idx ->
                    WorldCupGameContents.builder()
                            .name("contentsName" + idx)
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
            assertAll(
                    () -> assertThat(result.worldCupId()).isEqualTo(1L),

                    () -> assertThat(result.round()).isEqualTo(8),
                    () -> assertThat(result.title()).isEqualTo("title1"),
                    () -> assertThat(result.contentsList().size()).isEqualTo(8),

                    () -> assertThat(result.contentsList().get(0).getContentsId()).isEqualTo(1),
                    () -> assertThat(result.contentsList().get(0).getName()).isEqualTo("contentsName1"),
                    () -> assertThat(result.contentsList().get(0).getFilePath()).isEqualTo("/naver/.../1"),
                    () -> assertThat(result.contentsList().get(0).getInternetMovieStartPlayTime()).isEqualTo(null),
                    () -> assertThat(result.contentsList().get(0).getPlayDuration()).isEqualTo(null),

                    () -> assertThat(result.contentsList().get(7).getContentsId()).isEqualTo(8),
                    () -> assertThat(result.contentsList().get(7).getName()).isEqualTo("contentsName8"),
                    () -> assertThat(result.contentsList().get(7).getFilePath()).isEqualTo("/naver/.../8"),
                    () -> assertThat(result.contentsList().get(0).getInternetMovieStartPlayTime()).isEqualTo(null),
                    () -> assertThat(result.contentsList().get(0).getPlayDuration()).isEqualTo(null)
            );


        }

        @Test
        @DisplayName(SUCCESS_PREFIX + "8개 조회 (인터넷 동영상 URL 5개 + 정적 파일 5개)")
        public void success2() {

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

            List<StaticMediaFile> mediaFiles = range(1, 6).mapToObj(idx ->
                    StaticMediaFile.builder()
                            .originalName("fileOriginalName")
                            .absoluteName("fileAbsoluteName" + idx)
                            .filePath("/naver/.../" + idx)
                            .extension(".png")
                            .build()
            ).toList();

            List<InternetVideoUrl> internetMovieUrls = range(1, 6).mapToObj(idx ->
                    InternetVideoUrl.builder()
                            .videoPlayDuration(3)
                            .videoStartTime("00001")
                            .filePath("https://uTube.com/" + idx)
                            .build()
                    ).toList();

            List<WorldCupGameContents> contentsList1 = range(1, 6).mapToObj( idx ->
                    WorldCupGameContents.builder()
                            .name("contentsName" + idx)
                            .worldCupGame(worldCupGame)
                            .mediaFile(mediaFiles.get(idx - 1))
                            .build()
            ).toList();
            List<WorldCupGameContents> contentsList2 = range(5, 10).mapToObj( idx ->
                    WorldCupGameContents.builder()
                            .name("유튜브 영상 컨텐츠" + idx)
                            .worldCupGame(worldCupGame)
                            .mediaFile(internetMovieUrls.get(idx - 5))
                            .build()
            ).toList();

            worldCupGameRepository.save(worldCupGame);
            abstractMediaFileRepository.saveAll(mediaFiles);
            abstractMediaFileRepository.saveAll(internetMovieUrls);
            worldCupGameContentsRepository.saveAll(contentsList1);
            worldCupGameContentsRepository.saveAll(contentsList2);

            // when
            GetWorldCupPlayContentsResponse result = worldCupGamecontentsService.getPlayContents(
                    1L,
                    8,
                    1,
                    List.of()
            );

            // then
            assertAll(
                    () -> assertThat(result.worldCupId()).isEqualTo(1L),

                    () -> assertThat(result.round()).isEqualTo(8),
                    () -> assertThat(result.title()).isEqualTo("title1"),
                    () -> assertThat(result.contentsList().size()).isEqualTo(8),

                    () -> assertThat(result.contentsList().get(0).getContentsId()).isEqualTo(1),
                    () -> assertThat(result.contentsList().get(0).getName()).isEqualTo("contentsName1"),
                    () -> assertThat(result.contentsList().get(0).getFilePath()).isEqualTo("/naver/.../1"),
                    () -> assertThat(result.contentsList().get(0).getFileType()).isEqualTo("STATIC_MEDIA_FILE"),
                    () -> assertThat(result.contentsList().get(0).getInternetMovieStartPlayTime()).isEqualTo(null),
                    () -> assertThat(result.contentsList().get(0).getPlayDuration()).isEqualTo(null),

                    () -> assertThat(result.contentsList().get(7).getContentsId()).isEqualTo(8),
                    () -> assertThat(result.contentsList().get(7).getName()).isEqualTo("유튜브 영상 컨텐츠7"),
                    () -> assertThat(result.contentsList().get(7).getFilePath()).isEqualTo("https://uTube.com/3"),
                    () -> assertThat(result.contentsList().get(7).getFileType()).isEqualTo("INTERNET_VIDEO_URL"),
                    () -> assertThat(result.contentsList().get(7).getInternetMovieStartPlayTime()).isEqualTo("00001"),
                    () -> assertThat(result.contentsList().get(7).getPlayDuration()).isEqualTo(3)
            );


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

            List<StaticMediaFile> mediaFiles = range(1, 10).mapToObj(idx ->
                    StaticMediaFile.builder()
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
                    new GetDividedWorldCupGameContentsProjection(1, "name", "filePath", "MEDIA_FILE", null, null),
                    new GetDividedWorldCupGameContentsProjection(2, "name", "filePath", "MEDIA_FILE", null, null)
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

            assertThat(resultException.getPublicMessage()).contains("조회 컨텐츠 수가 다름 ");
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

            StaticMediaFile mediaFile1 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .absoluteName("fileAbsoluteName")
                    .filePath("filePath")
                    .extension("extension")
                    .build();
            StaticMediaFile mediaFile2 = StaticMediaFile.builder()
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
                    new GetDividedWorldCupGameContentsProjection(1, "name",  "filePath", "MEDIA_FILE", null, null),
                    new GetDividedWorldCupGameContentsProjection(2, "name", "filePath", "MEDIA_FILE", null, null)
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
            assertThat(resultException.getPublicMessage()).contains("컨텐츠 중복");
        }

    }

    @Nested
    @DisplayName("게임을 클리어할 수 있다.")
    public class clearWorldCupGame {
        @Test
        @DisplayName(SUCCESS_PREFIX)
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

            StaticMediaFile mediaFile1 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .absoluteName("fileAbsoluteName")
                    .filePath("filePath")
                    .extension("extension")
                    .build();
            StaticMediaFile mediaFile2 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .absoluteName("fileAbsoluteName")
                    .filePath("filePath")
                    .extension("extension")
                    .build();
            StaticMediaFile mediaFile3 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .absoluteName("fileAbsoluteName")
                    .filePath("filePath")
                    .extension("extension")
                    .build();
            StaticMediaFile mediaFile4 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .absoluteName("fileAbsoluteName")
                    .filePath("filePath")
                    .extension("extension")
                    .build();

            WorldCupGameContents contents1 = WorldCupGameContents.builder()
                    .name("contentsName1")
                    .worldCupGame(worldCupGame)
                    .mediaFile(mediaFile1)
                    .build();
            WorldCupGameContents contents2 = WorldCupGameContents.builder()
                    .name("contentsName2")
                    .worldCupGame(worldCupGame)
                    .mediaFile(mediaFile2)
                    .build();
            WorldCupGameContents contents3 = WorldCupGameContents.builder()
                    .name("contentsName3")
                    .worldCupGame(worldCupGame)
                    .mediaFile(mediaFile3)
                    .build();
            WorldCupGameContents contents4 = WorldCupGameContents.builder()
                    .name("contentsName4")
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
            ClearWorldCupGameResponse response = worldCupGamecontentsService.clearWorldCupGame(worldCupGame.getId(), request);

            // then
            String firstWinnerPoint = (String) ops.get(WINNER_CONTENTS_SCORE_KEY_FORMAT.formatted(1L, 1L));
            String secondWinnerPoint = (String) ops.get(WINNER_CONTENTS_SCORE_KEY_FORMAT.formatted(1L, 2L));
            String thirdWinnerPoint = (String) ops.get(WINNER_CONTENTS_SCORE_KEY_FORMAT.formatted(1L, 3L));
            String fourthWinnerPoint = (String) ops.get(WINNER_CONTENTS_SCORE_KEY_FORMAT.formatted(1L, 4L));

            assertAll(
                    () -> assertThat(firstWinnerPoint).isEqualTo("10"),
                    () -> assertThat(secondWinnerPoint).isEqualTo("7"),
                    () -> assertThat(thirdWinnerPoint).isEqualTo("4"),
                    () -> assertThat(fourthWinnerPoint).isEqualTo("4"),

                    () -> assertThat(response.firstWinnerName()).isEqualTo("contentsName1"),
                    () -> assertThat(response.secondWinnerName()).isEqualTo("contentsName2"),
                    () -> assertThat(response.thirdWinnerName()).isEqualTo("contentsName3"),
                    () -> assertThat(response.fourthWinnerName()).isEqualTo("contentsName4")
            );
        }

        @Test
        @DisplayName(EXCEPTION_PREFIX + "존재하지 않는 컨텐츠가 순위권이다.")
        public void fail1() {
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

            StaticMediaFile mediaFile1 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .absoluteName("fileAbsoluteName")
                    .filePath("filePath")
                    .extension("extension")
                    .build();
            StaticMediaFile mediaFile2 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .absoluteName("fileAbsoluteName")
                    .filePath("filePath")
                    .extension("extension")
                    .build();
            StaticMediaFile mediaFile3 = StaticMediaFile.builder()
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

            worldCupGameRepository.save(worldCupGame);
            mediaFileRepository.saveAll(List.of(mediaFile1, mediaFile2, mediaFile3));
            worldCupGameContentsRepository.saveAll(List.of(contents1, contents2, contents3));

            ClearWorldCupGameRequest request = ClearWorldCupGameRequest.builder()
                    .firstWinnerContentsId(1)
                    .secondWinnerContentsId(2)
                    .thirdWinnerContentsId(3)
                    .fourthWinnerContentsId(4)
                    .build();

            // when then
            assertThrows(
                    NotFoundWorldCupContentsException.class,
                    () -> worldCupGamecontentsService.clearWorldCupGame(worldCupGame.getId(), request)
            );
        }
    }

}
