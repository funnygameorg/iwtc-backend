package com.example.demo.worldcup.service;

import com.example.demo.domain.etc.model.InternetVideoUrl;
import com.example.demo.domain.etc.model.StaticMediaFile;
import com.example.demo.domain.etc.repository.AbstractMediaFileRepository;
import com.example.demo.domain.worldcup.model.WorldCupGame;
import com.example.demo.domain.worldcup.model.WorldCupGameContents;
import com.example.demo.domain.worldcup.repository.WorldCupGameContentsRepository;
import com.example.demo.domain.worldcup.repository.WorldCupGameRepository;
import com.example.demo.domain.worldcup.repository.projection.GetWorldCupGamePageProjection;
import com.example.demo.domain.worldcup.service.WorldCupGameService;
import com.example.demo.helper.DataBaseCleanUp;
import com.example.demo.helper.testbase.IntegrationBaseTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static com.example.demo.domain.worldcup.controller.vo.WorldCupDateRange.*;
import static com.example.demo.helper.TestConstant.SUCCESS_PREFIX;
import static com.example.demo.domain.worldcup.model.vo.VisibleType.PUBLIC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.Sort.Direction.*;

public class WorldCupGameServiceTest implements IntegrationBaseTest {

    @Autowired
    private WorldCupGameService worldCupGameService;
    @Autowired
    private WorldCupGameRepository worldCupGameRepository;
    @Autowired
    private WorldCupGameContentsRepository worldCupGameContentsRepository;
    @Autowired
    private AbstractMediaFileRepository abstractMediaFileRepository;
    @Autowired
    private DataBaseCleanUp dataBaseCleanUp;

    @AfterEach
    public void tearDown() {
        dataBaseCleanUp.truncateAllEntity();
    }

    @Nested
    @DisplayName("월드컵 리스트를 조회할 수 있다.")
    class findWorldCupByPageable {

        @Test
        @DisplayName(SUCCESS_PREFIX + "1개 조회")
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
                    .objectKey("filePath")
                    .extension("extension")
                    .build();
            StaticMediaFile mediaFile2 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .objectKey("fileAbsoluteName")
                    .extension("extension")
                    .build();

            WorldCupGameContents contents1 = WorldCupGameContents.builder()
                    .name("contentsName")
                    .worldCupGame(worldCupGame)
                    .mediaFile(mediaFile1)
                    .build();
            WorldCupGameContents contents2 = WorldCupGameContents.builder()
                    .name("contentsName2")
                    .worldCupGame(worldCupGame)
                    .mediaFile(mediaFile2)
                    .build();

            worldCupGameRepository.save(worldCupGame);
            abstractMediaFileRepository.saveAll(List.of(mediaFile1, mediaFile2));
            worldCupGameContentsRepository.saveAll(List.of(contents1, contents2));

            // when
            Page<GetWorldCupGamePageProjection> result = worldCupGameService.findWorldCupByPageable(
                    PageRequest.of(0, 25, DESC, "id"),
                    YEAR,
                    null,
                    null);

            // then
            assertThat(result.getContent().size()).isEqualTo(1);
        }

        @Test
        @DisplayName(SUCCESS_PREFIX + "2개 조회")
        public void success2() {
            // given
            WorldCupGame worldCupGame1 = WorldCupGame
                    .builder()
                    .title("title1")
                    .description("description1")
                    .visibleType(PUBLIC)
                    .views(0)
                    .softDelete(false)
                    .memberId(1)
                    .build();
            WorldCupGame worldCupGame2 = WorldCupGame
                    .builder()
                    .title("title2")
                    .description("description1")
                    .visibleType(PUBLIC)
                    .views(0)
                    .softDelete(false)
                    .memberId(1)
                    .build();

            StaticMediaFile mediaFile1 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .objectKey("fileAbsoluteName")
                    .extension("extension")
                    .build();
            StaticMediaFile mediaFile2 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .objectKey("fileAbsoluteName")
                    .extension("extension")
                    .build();
            StaticMediaFile mediaFile3 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .objectKey("fileAbsoluteName")
                    .extension("extension")
                    .build();
            StaticMediaFile mediaFile4 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .objectKey("fileAbsoluteName")
                    .extension("extension")
                    .build();

            WorldCupGameContents contents1 = WorldCupGameContents.builder()
                    .name("contentsName")
                    .worldCupGame(worldCupGame1)
                    .mediaFile(mediaFile1)
                    .build();
            WorldCupGameContents contents2 = WorldCupGameContents.builder()
                    .name("contentsName2")
                    .worldCupGame(worldCupGame1)
                    .mediaFile(mediaFile2)
                    .build();
            WorldCupGameContents contents3 = WorldCupGameContents.builder()
                    .name("contentsName3")
                    .worldCupGame(worldCupGame2)
                    .mediaFile(mediaFile3)
                    .build();
            WorldCupGameContents contents4 = WorldCupGameContents.builder()
                    .name("contentsName3")
                    .worldCupGame(worldCupGame2)
                    .mediaFile(mediaFile4)
                    .build();

            worldCupGameRepository.saveAll(List.of(worldCupGame1, worldCupGame2));
            abstractMediaFileRepository.saveAll(List.of(mediaFile1, mediaFile2, mediaFile3, mediaFile4));
            worldCupGameContentsRepository.saveAll(List.of(contents1, contents2, contents3, contents4));

            // when
            Page<GetWorldCupGamePageProjection> result = worldCupGameService.findWorldCupByPageable(
                    PageRequest.of(0, 25, DESC, "id"),
                    ALL,
                    null,
                    null);

            // then
            assertThat(result.getContent().size()).isEqualTo(2);
        }

        @Test
        @DisplayName(SUCCESS_PREFIX + "3개 조회")
        public void success3() {
            // given
            WorldCupGame worldCupGame1 = WorldCupGame
                    .builder()
                    .title("title1")
                    .description("description1")
                    .visibleType(PUBLIC)
                    .views(0)
                    .softDelete(false)
                    .memberId(1)
                    .build();
            WorldCupGame worldCupGame2 = WorldCupGame
                    .builder()
                    .title("title2")
                    .description("description2")
                    .visibleType(PUBLIC)
                    .views(0)
                    .softDelete(false)
                    .memberId(1)
                    .build();
            WorldCupGame worldCupGame3 = WorldCupGame
                    .builder()
                    .title("title3")
                    .description("description3")
                    .visibleType(PUBLIC)
                    .views(0)
                    .softDelete(false)
                    .memberId(1)
                    .build();

            StaticMediaFile mediaFile1 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .objectKey("fileAbsoluteName")
                    .extension("extension")
                    .build();
            StaticMediaFile mediaFile2 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .objectKey("fileAbsoluteName")
                    .extension("extension")
                    .build();
            StaticMediaFile mediaFile3 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .objectKey("fileAbsoluteName")
                    .extension("extension")
                    .build();

            InternetVideoUrl internetMovieUrl1 = InternetVideoUrl.builder()
                    .isPlayableVideo(true)
                    .videoStartTime("00001")
                    .videoPlayDuration(3)
                    .filePath("https://youtube/cats/1")
                    .build();
            InternetVideoUrl internetMovieUrl2 = InternetVideoUrl.builder()
                    .isPlayableVideo(true)
                    .videoStartTime("00001")
                    .videoPlayDuration(3)
                    .filePath("https://youtube/cats/2")
                    .build();
            InternetVideoUrl internetMovieUrl3 = InternetVideoUrl.builder()
                    .isPlayableVideo(true)
                    .videoStartTime("00001")
                    .videoPlayDuration(3)
                    .filePath("https://youtube/cats/3")
                    .build();

            WorldCupGameContents contents1 = WorldCupGameContents.builder()
                    .name("컨텐츠 네임 - 1")
                    .worldCupGame(worldCupGame1)
                    .mediaFile(mediaFile1)
                    .build();
            WorldCupGameContents contents2 = WorldCupGameContents.builder()
                    .name("컨텐츠 네임 - 2")
                    .worldCupGame(worldCupGame1)
                    .mediaFile(mediaFile2)
                    .build();
            WorldCupGameContents contents3 = WorldCupGameContents.builder()
                    .name("컨텐츠 네임 - 3")
                    .worldCupGame(worldCupGame2)
                    .mediaFile(internetMovieUrl1)
                    .build();
            WorldCupGameContents contents4 = WorldCupGameContents.builder()
                    .name("컨텐츠 네임 - 4")
                    .worldCupGame(worldCupGame2)
                    .mediaFile(internetMovieUrl2)
                    .build();
            WorldCupGameContents contents5 = WorldCupGameContents.builder()
                    .name("컨텐츠 네임 - 5")
                    .worldCupGame(worldCupGame3)
                    .mediaFile(mediaFile3)
                    .build();
            WorldCupGameContents contents6 = WorldCupGameContents.builder()
                    .name("컨텐츠 네임 - 6")
                    .worldCupGame(worldCupGame3)
                    .mediaFile(internetMovieUrl3)
                    .build();

            worldCupGameRepository.saveAll(List.of(worldCupGame1, worldCupGame2, worldCupGame3));
            abstractMediaFileRepository.saveAll(List.of(mediaFile1, mediaFile2, mediaFile3, internetMovieUrl1, internetMovieUrl2, internetMovieUrl3));
            worldCupGameContentsRepository.saveAll(List.of(contents1, contents2, contents3, contents4, contents5, contents6));

            // when
            Page<GetWorldCupGamePageProjection> result = worldCupGameService.findWorldCupByPageable(
                    PageRequest.of(0, 25, DESC, "id"),
                    ALL,
                    null,
                    null);

            // then
            assertThat(result.getContent().size()).isEqualTo(3);
        }

        @Test
        @DisplayName(SUCCESS_PREFIX + "memberId를 조건으로 조회")
        public void success4() {
            // given
            WorldCupGame worldCupGame1 = WorldCupGame
                    .builder()
                    .title("title1")
                    .description("description1")
                    .visibleType(PUBLIC)
                    .views(0)
                    .softDelete(false)
                    .memberId(1)
                    .build();
            WorldCupGame worldCupGame2 = WorldCupGame
                    .builder()
                    .title("title2")
                    .description("description2")
                    .visibleType(PUBLIC)
                    .views(0)
                    .softDelete(false)
                    .memberId(2)
                    .build();
            WorldCupGame worldCupGame3 = WorldCupGame
                    .builder()
                    .title("title3")
                    .description("description3")
                    .visibleType(PUBLIC)
                    .views(0)
                    .softDelete(false)
                    .memberId(3)
                    .build();

            StaticMediaFile mediaFile1 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .objectKey("fileAbsoluteName")
                    .extension("extension")
                    .build();
            StaticMediaFile mediaFile2 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .objectKey("fileAbsoluteName")
                    .extension("extension")
                    .build();
            StaticMediaFile mediaFile3 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .objectKey("fileAbsoluteName")
                    .extension("extension")
                    .build();

            InternetVideoUrl internetMovieUrl1 = InternetVideoUrl.builder()
                    .isPlayableVideo(true)
                    .videoStartTime("00001")
                    .videoPlayDuration(3)
                    .filePath("https://youtube/cats/1")
                    .build();
            InternetVideoUrl internetMovieUrl2 = InternetVideoUrl.builder()
                    .isPlayableVideo(true)
                    .videoStartTime("00001")
                    .videoPlayDuration(3)
                    .filePath("https://youtube/cats/2")
                    .build();
            InternetVideoUrl internetMovieUrl3 = InternetVideoUrl.builder()
                    .isPlayableVideo(true)
                    .videoStartTime("00001")
                    .videoPlayDuration(3)
                    .filePath("https://youtube/cats/3")
                    .build();

            WorldCupGameContents contents1 = WorldCupGameContents.builder()
                    .name("컨텐츠 네임 - 1")
                    .worldCupGame(worldCupGame1)
                    .mediaFile(mediaFile1)
                    .build();
            WorldCupGameContents contents2 = WorldCupGameContents.builder()
                    .name("컨텐츠 네임 - 2")
                    .worldCupGame(worldCupGame1)
                    .mediaFile(mediaFile2)
                    .build();
            WorldCupGameContents contents3 = WorldCupGameContents.builder()
                    .name("컨텐츠 네임 - 3")
                    .worldCupGame(worldCupGame2)
                    .mediaFile(internetMovieUrl1)
                    .build();
            WorldCupGameContents contents4 = WorldCupGameContents.builder()
                    .name("컨텐츠 네임 - 4")
                    .worldCupGame(worldCupGame2)
                    .mediaFile(internetMovieUrl2)
                    .build();
            WorldCupGameContents contents5 = WorldCupGameContents.builder()
                    .name("컨텐츠 네임 - 5")
                    .worldCupGame(worldCupGame3)
                    .mediaFile(mediaFile3)
                    .build();
            WorldCupGameContents contents6 = WorldCupGameContents.builder()
                    .name("컨텐츠 네임 - 6")
                    .worldCupGame(worldCupGame3)
                    .mediaFile(internetMovieUrl3)
                    .build();

            worldCupGameRepository.saveAll(List.of(worldCupGame1, worldCupGame2, worldCupGame3));
            abstractMediaFileRepository.saveAll(List.of(mediaFile1, mediaFile2, mediaFile3, internetMovieUrl1, internetMovieUrl2, internetMovieUrl3));
            worldCupGameContentsRepository.saveAll(List.of(contents1, contents2, contents3, contents4, contents5, contents6));

            // when
            Page<GetWorldCupGamePageProjection> result = worldCupGameService.findWorldCupByPageable(
                    PageRequest.of(0, 25, DESC, "id"),
                    ALL,
                    null,
                    3L
            );

            // then
            assertThat(result.getContent().size()).isEqualTo(1);
        }
    }

}
