package com.masikga.member.domain.worldcup.service;

import com.masikga.member.helper.DataBaseCleanUp;
import com.masikga.member.helper.testbase.ContainerBaseTest;
import com.masikga.member.helper.testbase.IntegrationBaseTest;
import com.masikga.worldcupgame.domain.etc.model.InternetVideoUrl;
import com.masikga.worldcupgame.domain.etc.model.StaticMediaFile;
import com.masikga.worldcupgame.domain.etc.repository.AbstractMediaFileRepository;
import com.masikga.worldcupgame.domain.etc.repository.MediaFileRepository;
import com.masikga.worldcupgame.domain.worldcup.controller.request.ClearWorldCupGameRequest;
import com.masikga.worldcupgame.domain.worldcup.controller.response.ClearWorldCupGameResponse;
import com.masikga.worldcupgame.domain.worldcup.controller.response.GetAvailableGameRoundsResponse;
import com.masikga.worldcupgame.domain.worldcup.controller.response.GetWorldCupPlayContentsResponse;
import com.masikga.worldcupgame.domain.worldcup.exception.IllegalWorldCupGameContentsExceptionMember;
import com.masikga.worldcupgame.domain.worldcup.exception.NoRoundsAvailableToPlayExceptionMember;
import com.masikga.worldcupgame.domain.worldcup.exception.NotFoundWorldCupContentsExceptionMember;
import com.masikga.worldcupgame.domain.worldcup.exception.NotFoundWorldCupGameExceptionMember;
import com.masikga.worldcupgame.domain.worldcup.model.WorldCupGame;
import com.masikga.worldcupgame.domain.worldcup.model.WorldCupGameContents;
import com.masikga.worldcupgame.domain.worldcup.repository.WorldCupGameContentsRepository;
import com.masikga.worldcupgame.domain.worldcup.repository.WorldCupGameRepository;
import com.masikga.worldcupgame.domain.worldcup.repository.projection.GetDividedWorldCupGameContentsProjection;
import com.masikga.worldcupgame.domain.worldcup.service.WorldCupGameContentsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;
import java.util.stream.LongStream;

import static com.masikga.member.helper.TestConstant.EXCEPTION_PREFIX;
import static com.masikga.member.helper.TestConstant.SUCCESS_PREFIX;
import static com.masikga.worldcupgame.domain.worldcup.model.vo.VisibleType.PUBLIC;
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

    @AfterEach
    public void tearDown() {
        dataBaseCleanUp.truncateAllEntity();
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
                                    .extension("PNG")
                                    .objectKey("/abc")
                                    .originalName("origin")
                                    .originalFileSize("0")
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
            var findWorldCupGame = worldCupGameRepository.findById(1L).get();

            // then
            assertAll(
                    () -> assertThat(result.worldCupId()).isEqualTo(1),
                    () -> assertThat(result.worldCupDescription()).isEqualTo("description1"),
                    () -> assertThat(result.rounds()).isEqualTo(List.of(2, 4, 8)),
                    () -> assertThat(findWorldCupGame.getViews()).isEqualTo(1)
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
                    NoRoundsAvailableToPlayExceptionMember.class,
                    () -> worldCupGamecontentsService.getAvailableGameRounds(1L)
            );
        }

        @Test
        @DisplayName(EXCEPTION_PREFIX + "존재하지 않는 게임을 조회할 수 없음")
        public void fail2() {
            // when
            assertThrows(
                    NotFoundWorldCupGameExceptionMember.class,
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
                            .objectKey("fileAbsoluteName" + idx)
                            .extension("MP4")
                            .originalFileSize("0")
                            .build()
            ).toList();

            List<WorldCupGameContents> contentsList = range(1, 10).mapToObj(idx ->
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

            var firstContents = result.contentsList().stream()
                    .filter(contents -> contents.getContentsId() == 1)
                    .findFirst()
                    .get();
            var sevenContents = result.contentsList().stream()
                    .filter(contents -> contents.getContentsId() == 8)
                    .findFirst()
                    .get();

            // then
            assertAll(
                    () -> assertThat(result.worldCupId()).isEqualTo(1L),

                    () -> assertThat(result.round()).isEqualTo(8),
                    () -> assertThat(result.title()).isEqualTo("title1"),
                    () -> assertThat(result.contentsList().size()).isEqualTo(8),

                    () -> assertThat(firstContents.getContentsId()).isEqualTo(1),
                    () -> assertThat(firstContents.getName()).isEqualTo("contentsName1"),
                    () -> assertThat(firstContents.getMediaFileId()).isEqualTo(1),
                    () -> assertThat(firstContents.getInternetMovieStartPlayTime()).isEqualTo(null),
                    () -> assertThat(firstContents.getPlayDuration()).isEqualTo(null),

                    () -> assertThat(sevenContents.getContentsId()).isEqualTo(8),
                    () -> assertThat(sevenContents.getName()).isEqualTo("contentsName8"),
                    () -> assertThat(sevenContents.getMediaFileId()).isEqualTo(8),
                    () -> assertThat(sevenContents.getInternetMovieStartPlayTime()).isEqualTo(null),
                    () -> assertThat(sevenContents.getPlayDuration()).isEqualTo(null)
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
                            .objectKey("fileAbsoluteName" + idx)
                            .extension("GIF")
                            .originalFileSize("0")
                            .build()
            ).toList();

            List<InternetVideoUrl> internetMovieUrls = range(1, 6).mapToObj(idx ->
                    InternetVideoUrl.builder()
                            .videoPlayDuration(3)
                            .videoStartTime("00001")
                            .objectKey("https://uTube.com/" + idx)
                            .videoDetailType("YOU_TUBE_URL")
                            .originalFileSize("0")
                            .build()
            ).toList();

            List<WorldCupGameContents> contentsList1 = range(1, 6).mapToObj(idx ->
                    WorldCupGameContents.builder()
                            .name("contentsName" + idx)
                            .worldCupGame(worldCupGame)
                            .mediaFile(mediaFiles.get(idx - 1))
                            .build()
            ).toList();
            List<WorldCupGameContents> contentsList2 = range(5, 10).mapToObj(idx ->
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

            var firstContents = result.contentsList().stream()
                    .filter(contents -> contents.getContentsId() == 1)
                    .findFirst()
                    .get();
            var sevenContents = result.contentsList().stream()
                    .filter(contents -> contents.getContentsId() == 8)
                    .findFirst()
                    .get();

            // then
            assertAll(
                    () -> assertThat(result.worldCupId()).isEqualTo(1L),

                    () -> assertThat(result.round()).isEqualTo(8),
                    () -> assertThat(result.title()).isEqualTo("title1"),
                    () -> assertThat(result.contentsList().size()).isEqualTo(8),

                    () -> assertThat(firstContents.getContentsId()).isEqualTo(1),
                    () -> assertThat(firstContents.getName()).isEqualTo("contentsName1"),
                    () -> assertThat(firstContents.getMediaFileId()).isEqualTo(1),
                    () -> assertThat(firstContents.getFileType()).isEqualTo("STATIC_MEDIA_FILE"),
                    () -> assertThat(firstContents.getInternetMovieStartPlayTime()).isEqualTo(null),
                    () -> assertThat(firstContents.getPlayDuration()).isEqualTo(null),

                    () -> assertThat(sevenContents.getContentsId()).isEqualTo(8),
                    () -> assertThat(sevenContents.getName()).isEqualTo("유튜브 영상 컨텐츠7"),
                    () -> assertThat(sevenContents.getMediaFileId()).isEqualTo(8),
                    () -> assertThat(sevenContents.getFileType()).isEqualTo("INTERNET_VIDEO_URL"),
                    () -> assertThat(sevenContents.getInternetMovieStartPlayTime()).isEqualTo("00001"),
                    () -> assertThat(sevenContents.getPlayDuration()).isEqualTo(3)
            );

        }

        @Test
        @DisplayName(SUCCESS_PREFIX + "컨텐츠의 순서가 섞여있다.")
        public void success3() {

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
                            .objectKey("fileAbsoluteName" + idx)
                            .extension("GIF")
                            .originalFileSize("0")
                            .build()
            ).toList();

            List<InternetVideoUrl> internetMovieUrls = range(1, 6).mapToObj(idx ->
                    InternetVideoUrl.builder()
                            .videoPlayDuration(3)
                            .videoStartTime("00001")
                            .objectKey("https://uTube.com/" + idx)
                            .videoDetailType("YOU_TUBE_URL")
                            .originalFileSize("0")
                            .build()
            ).toList();

            List<WorldCupGameContents> contentsList1 = range(1, 6).mapToObj(idx ->
                    WorldCupGameContents.builder()
                            .name("contentsName" + idx)
                            .worldCupGame(worldCupGame)
                            .mediaFile(mediaFiles.get(idx - 1))
                            .build()
            ).toList();

            List<WorldCupGameContents> contentsList2 = range(5, 10).mapToObj(idx ->
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

            var actualContentsIds = result.contentsList()
                    .stream()
                    .map(GetWorldCupPlayContentsResponse.PlayContents::getContentsId)
                    .toList();

            var sortedAscendingNumbers = LongStream.range(1l, 9L)
                    .mapToObj(contentsId -> contentsId)
                    .toList();

            // then
            assertAll(
                    () -> assertThat(result.worldCupId()).isEqualTo(1L),

                    () -> assertThat(result.round()).isEqualTo(8),
                    () -> assertThat(result.title()).isEqualTo("title1"),
                    () -> assertThat(result.contentsList().size()).isEqualTo(8),

                    () -> assertThat(actualContentsIds).isNotEqualTo(sortedAscendingNumbers)
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
                            .objectKey("fileAbsoluteName")
                            .extension("GIF")
                            .originalFileSize("0")
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
                    new GetDividedWorldCupGameContentsProjection(1, "name", 1, "MEDIA_FILE", null, null),
                    new GetDividedWorldCupGameContentsProjection(2, "name", 2, "MEDIA_FILE", null, null)
            );
            given(worldCupGameRepository.getDividedWorldCupGameContentsV2(1L, 8, List.of()))
                    .willReturn(ACTUAL_GET_CONTENTS_LIST);

            // when & then
            IllegalWorldCupGameContentsExceptionMember resultException =
                    assertThrows(
                            IllegalWorldCupGameContentsExceptionMember.class,
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
                    .objectKey("fileAbsoluteName")
                    .extension("GIF")
                    .originalFileSize("0")
                    .build();
            StaticMediaFile mediaFile2 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .objectKey("fileAbsoluteName")
                    .extension("MP4")
                    .originalFileSize("0")
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
                    new GetDividedWorldCupGameContentsProjection(1, "name", 1, "MEDIA_FILE", null, null),
                    new GetDividedWorldCupGameContentsProjection(2, "name", 2, "MEDIA_FILE", null, null)
            );
            given(worldCupGameRepository.getDividedWorldCupGameContentsV2(1L, 2, List.of(1L)))
                    .willReturn(ACTUAL_GET_CONTENTS_LIST);

            worldCupGameRepository.save(worldCupGame);
            mediaFileRepository.saveAll(List.of(mediaFile1, mediaFile2));
            worldCupGameContentsRepository.saveAll(List.of(contents1, contents2));

            // when & then
            final List<Long> ALREADY_PLAYED_CONTENTS_ID = List.of(1L);
            IllegalWorldCupGameContentsExceptionMember resultException =
                    assertThrows(
                            IllegalWorldCupGameContentsExceptionMember.class,
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
                    .objectKey("fileAbsoluteName")
                    .extension("JPG")
                    .originalFileSize("0")
                    .build();
            StaticMediaFile mediaFile2 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .objectKey("fileAbsoluteName")
                    .extension("JPEG")
                    .originalFileSize("0")
                    .build();
            StaticMediaFile mediaFile3 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .objectKey("fileAbsoluteName")
                    .extension("PNG")
                    .originalFileSize("0")
                    .build();
            StaticMediaFile mediaFile4 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .objectKey("fileAbsoluteName")
                    .extension("MP4")
                    .originalFileSize("0")
                    .build();

            WorldCupGameContents contents1 = WorldCupGameContents.builder()
                    .name("contentsName1")
                    .worldCupGame(worldCupGame)
                    .mediaFile(mediaFile1)
                    .gameScore(0)
                    .build();
            WorldCupGameContents contents2 = WorldCupGameContents.builder()
                    .name("contentsName2")
                    .worldCupGame(worldCupGame)
                    .mediaFile(mediaFile2)
                    .gameScore(0)
                    .build();
            WorldCupGameContents contents3 = WorldCupGameContents.builder()
                    .name("contentsName3")
                    .worldCupGame(worldCupGame)
                    .mediaFile(mediaFile3)
                    .gameScore(0)
                    .build();
            WorldCupGameContents contents4 = WorldCupGameContents.builder()
                    .name("contentsName4")
                    .worldCupGame(worldCupGame)
                    .mediaFile(mediaFile4)
                    .gameScore(0)
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
            List<ClearWorldCupGameResponse> response = worldCupGamecontentsService.clearWorldCupGame(
                    worldCupGame.getId(), request);

            // then
            String firstWinnerPoint = String.valueOf(worldCupGameContentsRepository.findById(1L).get().getGameScore());
            String secondWinnerPoint = String.valueOf(worldCupGameContentsRepository.findById(2L).get().getGameScore());
            String thirdWinnerPoint = String.valueOf(worldCupGameContentsRepository.findById(3L).get().getGameScore());
            String fourthWinnerPoint = String.valueOf(worldCupGameContentsRepository.findById(4L).get().getGameScore());

            ClearWorldCupGameResponse firstWinner = response.stream()
                    .filter(winner -> winner.rank() == 1)
                    .toList()
                    .get(0);
            ClearWorldCupGameResponse secondWinner = response.stream()
                    .filter(winner -> winner.rank() == 2)
                    .toList()
                    .get(0);
            ClearWorldCupGameResponse thirdWinner = response.stream()
                    .filter(winner -> winner.rank() == 3)
                    .toList()
                    .get(0);
            ClearWorldCupGameResponse fourthWinner = response.stream()
                    .filter(winner -> winner.rank() == 4)
                    .toList()
                    .get(0);

            assertAll(
                    () -> assertThat(firstWinnerPoint).isEqualTo("10"),
                    () -> assertThat(secondWinnerPoint).isEqualTo("7"),
                    () -> assertThat(thirdWinnerPoint).isEqualTo("4"),
                    () -> assertThat(fourthWinnerPoint).isEqualTo("4"),

                    () -> assertThat(firstWinner.contentsName()).isEqualTo("contentsName1"),
                    () -> assertThat(firstWinner.contentsId()).isEqualTo(1),
                    () -> assertThat(firstWinner.mediaFileId()).isEqualTo(1),

                    () -> assertThat(secondWinner.contentsName()).isEqualTo("contentsName2"),
                    () -> assertThat(secondWinner.contentsId()).isEqualTo(2),
                    () -> assertThat(secondWinner.mediaFileId()).isEqualTo(2),

                    () -> assertThat(thirdWinner.contentsName()).isEqualTo("contentsName3"),
                    () -> assertThat(thirdWinner.contentsId()).isEqualTo(3),
                    () -> assertThat(thirdWinner.mediaFileId()).isEqualTo(3),

                    () -> assertThat(fourthWinner.contentsName()).isEqualTo("contentsName4"),
                    () -> assertThat(fourthWinner.contentsId()).isEqualTo(4),
                    () -> assertThat(fourthWinner.mediaFileId()).isEqualTo(4)
            );
        }

        @Test
        @DisplayName(EXCEPTION_PREFIX + "존재하지 않는 컨텐츠가 순위권이다.")
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

            StaticMediaFile mediaFile1 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .objectKey("fileAbsoluteName")
                    .extension("GIF")
                    .originalFileSize("0")
                    .build();
            StaticMediaFile mediaFile2 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .objectKey("fileAbsoluteName")
                    .extension("GIF")
                    .originalFileSize("0")
                    .build();
            StaticMediaFile mediaFile3 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .objectKey("fileAbsoluteName")
                    .extension("PNG")
                    .originalFileSize("0")
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
                    NotFoundWorldCupContentsExceptionMember.class,
                    () -> worldCupGamecontentsService.clearWorldCupGame(worldCupGame.getId(), request)
            );
        }
    }

    @Nested
    @DisplayName("월드컵 게임에 포함된 컨텐츠를 순위로 정렬해서 조회할 수 있다.")
    public class GetGameResultContentsList {

        @Test
        @DisplayName(SUCCESS_PREFIX)
        public void success1() {

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
                    .objectKey("fileAbsoluteName")
                    .extension("PNG")
                    .originalFileSize("0")
                    .build();
            StaticMediaFile mediaFile2 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .objectKey("fileAbsoluteName")
                    .extension("PNG")
                    .originalFileSize("0")
                    .build();
            StaticMediaFile mediaFile3 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .objectKey("fileAbsoluteName")
                    .extension("GIF")
                    .originalFileSize("0")
                    .build();

            WorldCupGameContents contents1 = WorldCupGameContents.builder()
                    .name("컨텐츠1")
                    .worldCupGame(worldCupGame)
                    .gameRank(0)
                    .gameScore(150)
                    .mediaFile(mediaFile1)
                    .build();
            WorldCupGameContents contents2 = WorldCupGameContents.builder()
                    .name("컨텐츠2")
                    .worldCupGame(worldCupGame)
                    .gameRank(0)
                    .gameScore(112)
                    .mediaFile(mediaFile2)
                    .build();
            WorldCupGameContents contents3 = WorldCupGameContents.builder()
                    .name("컨텐츠3")
                    .worldCupGame(worldCupGame)
                    .gameRank(0)
                    .gameScore(320)
                    .mediaFile(mediaFile3)
                    .build();

            worldCupGameRepository.save(worldCupGame);
            mediaFileRepository.saveAll(List.of(mediaFile1, mediaFile2, mediaFile3));
            worldCupGameContentsRepository.saveAll(List.of(contents1, contents2, contents3));

            // when
            var result = worldCupGamecontentsService.getGameResultContentsList(1L);

            // then
            assertAll(
                    () -> assertThat(result.size()).isEqualTo(3),

                    () -> assertThat(result.get(0).contentsName()).isEqualTo("컨텐츠3"),
                    () -> assertThat(result.get(0).gameScore()).isEqualTo(320),
                    () -> assertThat(result.get(0).mediaFileId()).isEqualTo(3),
                    () -> assertThat(result.get(0).gameRank()).isEqualTo(1),

                    () -> assertThat(result.get(1).contentsName()).isEqualTo("컨텐츠1"),
                    () -> assertThat(result.get(1).gameScore()).isEqualTo(150),
                    () -> assertThat(result.get(1).mediaFileId()).isEqualTo(1),
                    () -> assertThat(result.get(1).gameRank()).isEqualTo(2),

                    () -> assertThat(result.get(2).contentsName()).isEqualTo("컨텐츠2"),
                    () -> assertThat(result.get(2).gameScore()).isEqualTo(112),
                    () -> assertThat(result.get(2).mediaFileId()).isEqualTo(2),
                    () -> assertThat(result.get(2).gameRank()).isEqualTo(3)
            );

        }

        @Test
        @DisplayName(EXCEPTION_PREFIX + "존재하지 않는 월드컵은 조회할 수 없다.")
        public void fail1() {

            // when & then
            assertThrows(
                    NotFoundWorldCupGameExceptionMember.class,
                    () -> worldCupGamecontentsService.getGameResultContentsList(1L)
            );

        }

    }

}
