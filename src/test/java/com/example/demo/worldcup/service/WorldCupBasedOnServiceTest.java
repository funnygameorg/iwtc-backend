package com.example.demo.worldcup.service;

import com.example.demo.domain.etc.model.InternetVideoUrl;
import com.example.demo.domain.etc.model.MediaFile;
import com.example.demo.domain.etc.model.StaticMediaFile;
import com.example.demo.domain.etc.model.vo.FileType;
import com.example.demo.domain.etc.repository.MediaFileRepository;
import com.example.demo.domain.worldcup.component.RandomDataGenerator;
import com.example.demo.domain.worldcup.component.RandomDataGeneratorInterface;
import com.example.demo.domain.worldcup.controller.request.CreateWorldCupContentsRequest;
import com.example.demo.domain.worldcup.controller.request.CreateWorldCupRequest;
import com.example.demo.domain.worldcup.controller.response.GetWorldCupContentsResponse;
import com.example.demo.domain.worldcup.exception.DuplicatedWorldCupGameTitleException;
import com.example.demo.domain.worldcup.exception.NotFoundWorldCupGameException;
import com.example.demo.domain.worldcup.exception.NotOwnerGameException;
import com.example.demo.domain.worldcup.model.WorldCupGame;
import com.example.demo.domain.worldcup.model.WorldCupGameContents;
import com.example.demo.domain.worldcup.model.vo.VisibleType;
import com.example.demo.domain.worldcup.repository.WorldCupGameContentsRepository;
import com.example.demo.domain.worldcup.repository.WorldCupGameRepository;
import com.example.demo.domain.worldcup.service.WorldCupBasedOnAuthService;
import com.example.demo.helper.DataBaseCleanUp;
import com.example.demo.helper.testbase.IntegrationBaseTest;
import com.example.demo.infra.s3.S3Component;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.demo.domain.etc.model.vo.FileType.INTERNET_VIDEO_URL;
import static com.example.demo.domain.etc.model.vo.FileType.STATIC_MEDIA_FILE;
import static com.example.demo.domain.worldcup.controller.request.CreateWorldCupContentsRequest.*;
import static com.example.demo.domain.worldcup.model.vo.VisibleType.PRIVATE;
import static com.example.demo.domain.worldcup.model.vo.VisibleType.PUBLIC;
import static com.example.demo.helper.TestConstant.EXCEPTION_PREFIX;
import static com.example.demo.helper.TestConstant.SUCCESS_PREFIX;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.BDDMockito.given;

public class WorldCupBasedOnServiceTest implements IntegrationBaseTest {

    @Autowired
    private WorldCupBasedOnAuthService worldCupBasedOnAuthService;
    @Autowired
    private WorldCupGameRepository worldCupGameRepository;
    @Autowired
    private MediaFileRepository mediaFileRepository;
    @Autowired
    private WorldCupGameContentsRepository worldCupGameContentsRepository;
    @MockBean
    private S3Component s3Component;
    @Autowired
    private DataBaseCleanUp dataBaseCleanUp;

    @AfterEach
    public void tearDown() {
        dataBaseCleanUp.truncateAllEntity();
    }

    @Nested
    @DisplayName("월드컵 게임 수정 화면에 사용되는 컨텐츠 리스트 조회할 수 있다.")
    public class getMyWorldCupGameContents {

        @Test
        @DisplayName(SUCCESS_PREFIX)
        public void success1() {
            // given
            WorldCupGame worldCupGame = WorldCupGame.builder()
                    .title("게임1")
                    .description("설명1")
                    .visibleType(PUBLIC)
                    .memberId(1)
                    .build();
            StaticMediaFile mediaFile1 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .objectKey("https://www.abc.com/BS/1")
                    .extension("extension")
                    .build();
            StaticMediaFile mediaFile2 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .objectKey("https://www.abc.com/BS/2")
                    .extension("extension")
                    .build();
            WorldCupGameContents contents1 = WorldCupGameContents.builder()
                    .name("컨텐츠1")
                    .gameScore(10)
                    .gameRank(1)
                    .visibleType(PUBLIC)
                    .worldCupGame(worldCupGame)
                    .mediaFile(mediaFile1)
                    .build();
            WorldCupGameContents contents2  = WorldCupGameContents.builder()
                    .name("컨텐츠2")
                    .gameScore(5)
                    .gameRank(2)
                    .visibleType(PUBLIC)
                    .worldCupGame(worldCupGame)
                    .mediaFile(mediaFile2)
                    .build();
            worldCupGameRepository.save(worldCupGame);
            mediaFileRepository.saveAll(List.of(mediaFile1, mediaFile2));
            worldCupGameContentsRepository.saveAll(List.of(contents1, contents2));

            // when
            List<GetWorldCupContentsResponse> response = worldCupBasedOnAuthService.getMyWorldCupGameContents(1, 1);

            // then
            GetWorldCupContentsResponse firstElement = response.get(0);
            GetWorldCupContentsResponse secondElement = response.get(1);

            assertAll(
                    () -> assertThat(response.size(), is(2)),

                    () -> assertThat(firstElement.contentsId(), is(1L)),
                    () -> assertThat(firstElement.contentsName(), is("컨텐츠1")),
                    () -> assertThat(firstElement.rank(), is(1)),
                    () -> assertThat(firstElement.score(), is(10)),
                    () -> assertThat(firstElement.fileResponse().mediaFileId(), is(1L)),
                    () -> assertThat(firstElement.fileResponse().filePath(), is("https://www.abc.com/BS/1")),
                    () -> assertThat(firstElement.fileResponse().createdAt(), is(any(LocalDateTime.class))),
                    () -> assertThat(firstElement.fileResponse().updatedAt(), is(any(LocalDateTime.class))),

                    () -> assertThat(secondElement.contentsId(), is(2L)),
                    () -> assertThat(secondElement.contentsName(), is("컨텐츠2")),
                    () -> assertThat(secondElement.rank(), is(2)),
                    () -> assertThat(secondElement.score(), is(5)),
                    () -> assertThat(secondElement.fileResponse().mediaFileId(), is(2L)),
                    () -> assertThat(secondElement.fileResponse().filePath(), is("https://www.abc.com/BS/2")),
                    () -> assertThat(secondElement.fileResponse().createdAt(), is(is(any(LocalDateTime.class)))),
                    () -> assertThat(secondElement.fileResponse().updatedAt(), is(is(any(LocalDateTime.class))))
            );
        }

        @Test
        @DisplayName(EXCEPTION_PREFIX + "월드컵 게임 작성자가 아닌 것은 조회할 수 없다.")
        public void fail2() {

            // given
            WorldCupGame worldCupGame = WorldCupGame.builder()
                    .title("게임1")
                    .description("설명1")
                    .visibleType(PUBLIC)
                    .memberId(2)
                    .build();

            StaticMediaFile mediaFile1 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .objectKey("filePath")
                    .extension("extension")
                    .build();

            StaticMediaFile mediaFile2 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .objectKey("filePath")
                    .extension("extension")
                    .build();

            WorldCupGameContents contents1 = WorldCupGameContents.builder()
                    .name("컨텐츠1")
                    .gameScore(1)
                    .visibleType(PUBLIC)
                    .worldCupGame(worldCupGame)
                    .mediaFile(mediaFile1)
                    .build();

            WorldCupGameContents contents2  = WorldCupGameContents.builder()
                    .name("컨텐츠2")
                    .gameScore(1)
                    .visibleType(PUBLIC)
                    .worldCupGame(worldCupGame)
                    .mediaFile(mediaFile2)
                    .build();

            worldCupGameRepository.save(worldCupGame);
            mediaFileRepository.saveAll(List.of(mediaFile1, mediaFile2));
            worldCupGameContentsRepository.saveAll(List.of(contents1, contents2));

            long worldCupId = 1;
            long memberId = 1;

            // when
            assertThrows(
                    NotOwnerGameException.class,
                    ()-> worldCupBasedOnAuthService.getMyWorldCupGameContents(worldCupId, memberId)
            );

        }
    }

    @Nested
    @DisplayName("월드컵 게임을 생성할 수 있다.")
    public class createWorldCup {

        @Test
        @DisplayName(SUCCESS_PREFIX)
        public void success1() {

            // given
            CreateWorldCupRequest request = CreateWorldCupRequest.builder()
                    .title("뱀")
                    .description("사자")
                    .visibleType(PRIVATE)
                    .build();

            // when
            worldCupBasedOnAuthService.createMyWorldCup(request, 1L);

            // then
            WorldCupGame updatedGame = worldCupGameRepository.findById(1L).get();
            assertAll(
                    () -> assertThat(updatedGame.getTitle()).isEqualTo("뱀"),
                    () -> assertThat(updatedGame.getDescription()).isEqualTo("사자"),
                    () -> assertThat(updatedGame.getVisibleType()).isEqualTo(PRIVATE)
            );
        }

        @Test
        @DisplayName(EXCEPTION_PREFIX + "이미 존재하는 월드컵 게임 타이틀은 사용할 수 없다.")
        public void fail2() {

            // given
            CreateWorldCupRequest request = CreateWorldCupRequest.builder()
                    .title("호랑이")
                    .description("뱀")
                    .visibleType(PRIVATE)
                    .build();

            WorldCupGame worldCupGame = WorldCupGame.builder()
                    .title("호랑이")
                    .description("개미")
                    .visibleType(PUBLIC)
                    .memberId(1)
                    .build();

            worldCupGameRepository.save(worldCupGame);

            // when then
            assertThrows(
                    DuplicatedWorldCupGameTitleException.class,
                    () -> worldCupBasedOnAuthService.putMyWorldCup(request, 2L, 1L)
            );
        }

    }

    @Nested
    @DisplayName("월드컵 게임을 수정할 수 있다.")
    public class putWorldCup {

        @Test
        @DisplayName(SUCCESS_PREFIX)
        public void success2() {

            // given
            CreateWorldCupRequest request = CreateWorldCupRequest.builder()
                    .title("원숭이")
                    .description("토끼")
                    .visibleType(PRIVATE)
                    .build();

            WorldCupGame worldCupGame = WorldCupGame.builder()
                    .title("호랑이")
                    .description("개미")
                    .visibleType(PUBLIC)
                    .memberId(1)
                    .build();

            worldCupGameRepository.save(worldCupGame);

            // when
            worldCupBasedOnAuthService.putMyWorldCup(request, 1L, 1L);

            // then
            WorldCupGame updatedGame = worldCupGameRepository.findById(1L).get();
            assertAll(
                    () -> assertThat(updatedGame.getTitle()).isEqualTo("원숭이"),
                    () -> assertThat(updatedGame.getDescription()).isEqualTo("토끼"),
                    () -> assertThat(updatedGame.getVisibleType()).isEqualTo(PRIVATE)
            );
        }

        @Test
        @DisplayName(EXCEPTION_PREFIX + "자신이 작성한 게임이 아니면 수정할 수 없다.")
        public void fail1() {

            // given
            CreateWorldCupRequest request = CreateWorldCupRequest.builder()
                    .title("원숭이")
                    .description("토끼")
                    .visibleType(PRIVATE)
                    .build();

            WorldCupGame worldCupGame = WorldCupGame.builder()
                    .title("호랑이")
                    .description("개미")
                    .visibleType(PUBLIC)
                    .memberId(1)
                    .build();

            worldCupGameRepository.save(worldCupGame);

            // when then
            assertThrows(
                    NotOwnerGameException.class,
                    () ->worldCupBasedOnAuthService.putMyWorldCup(request, 1L, 3L)
            );
        }

        @Test
        @DisplayName(EXCEPTION_PREFIX + "이미 존재하는 월드컵 게임 타이틀은 사용할 수 없다.")
        public void fail2() {

            // given
            CreateWorldCupRequest request = CreateWorldCupRequest.builder()
                    .title("호랑이")
                    .description("뱀")
                    .visibleType(PRIVATE)
                    .build();

            WorldCupGame worldCupGame = WorldCupGame.builder()
                    .title("호랑이")
                    .description("개미")
                    .visibleType(PUBLIC)
                    .memberId(1)
                    .build();

            worldCupGameRepository.save(worldCupGame);

            // when then
            assertThrows(
                    DuplicatedWorldCupGameTitleException.class,
                    () -> worldCupBasedOnAuthService.putMyWorldCup(request, 2L, 1L)
            );
        }

        @Test
        @DisplayName(EXCEPTION_PREFIX + "존재하지 않는 월드컵을 수정할 수 없다.")
        public void fail3() {

            // given
            CreateWorldCupRequest request = CreateWorldCupRequest.builder()
                    .title("철쭉 게임")
                    .description("뱀")
                    .visibleType(PRIVATE)
                    .build();

            WorldCupGame worldCupGame = WorldCupGame.builder()
                    .title("호랑이")
                    .description("개미")
                    .visibleType(PUBLIC)
                    .memberId(1)
                    .build();

            worldCupGameRepository.save(worldCupGame);

            // when then
            assertThrows(
                    NotFoundWorldCupGameException.class,
                    () -> worldCupBasedOnAuthService.putMyWorldCup(request, 2L, 1L)
            );
        }

    }



    @Nested
    @DisplayName("월드컵 게임 컨텐츠를 생성할 수 있다.")
    public class createWorldCupGameContents {




        @Test
        @DisplayName(SUCCESS_PREFIX)
        public void success1() {

            // given
            given(s3Component.putObject(anyString(), anyString()))
                    .willReturn(null);

            var createStaticMediaFileContents = CreateContentsRequest.builder()
                            .contentsName("컨텐츠 이름1")
                            .visibleType(PUBLIC)
                            .createMediaFileRequest(
                                    CreateMediaFileRequest.builder()
                                            .fileType(STATIC_MEDIA_FILE)
                                            .mediaData("base64:jpg...abcd")
                                            .originalName("Original1")
                                            .build()
                            )
                            .build();

            var createInternetVideoUrlContents = CreateContentsRequest.builder()
                            .contentsName("컨텐츠 이름2")
                            .visibleType(PUBLIC)
                            .createMediaFileRequest(
                                    CreateMediaFileRequest.builder()
                                            .fileType(INTERNET_VIDEO_URL)
                                            .mediaData("https://filepaths/2")
                                            .videoPlayDuration(3)
                                            .videoStartTime("00100")
                                            .build()
                            )
                            .build();

            var request = builder()
                    .data(List.of(createStaticMediaFileContents, createInternetVideoUrlContents))
                    .build();

            var worldCupGame = WorldCupGame.builder()
                    .title("title1")
                    .visibleType(PUBLIC)
                    .memberId(1)
                    .build();

            worldCupGameRepository.save(worldCupGame);


            // when
            worldCupBasedOnAuthService.createMyWorldCupContents(request, 1, 1);


            // then
            List<WorldCupGameContents> contentsList = worldCupGameContentsRepository.findAll();

            StaticMediaFile firstMediaFile = (StaticMediaFile) mediaFileRepository.findById(contentsList.get(0).getMediaFile().getId()).get();

            InternetVideoUrl secondMediaFile = (InternetVideoUrl) mediaFileRepository.findById(contentsList.get(1).getMediaFile().getId()).get();

            assertAll(
                    () -> assertThat(contentsList.get(0).getId()).isEqualTo(1),
                    () -> assertThat(contentsList.get(0).getName()).isEqualTo("컨텐츠 이름1"),
                    () -> assertThat(contentsList.get(0).getVisibleType()).isEqualTo(PUBLIC),
                    () -> assertThat(contentsList.get(0).getGameRank()).isEqualTo(0),
                    () -> assertThat(contentsList.get(0).getGameScore()).isEqualTo(0),

                    () -> assertThat(firstMediaFile.getId()).isEqualTo(1),
                    () -> assertThat(firstMediaFile.getObjectKey()).isNotNull(),
                    () -> assertThat(firstMediaFile.getFileType()).isEqualTo(STATIC_MEDIA_FILE),
                    () -> assertThat(firstMediaFile.getOriginalName()).isEqualTo("Original1"),
                    () -> assertThat(firstMediaFile.getExtension()).isEqualTo("tempExtension"),

                    
                    () -> assertThat(contentsList.get(1).getId()).isEqualTo(2),
                    () -> assertThat(contentsList.get(1).getName()).isEqualTo("컨텐츠 이름2"),
                    () -> assertThat(contentsList.get(1).getVisibleType()).isEqualTo(PUBLIC),
                    () -> assertThat(contentsList.get(1).getGameRank()).isEqualTo(0),
                    () -> assertThat(contentsList.get(1).getGameScore()).isEqualTo(0),

                    () -> assertThat(secondMediaFile.getId()).isEqualTo(2),
                    () -> assertThat(secondMediaFile.getObjectKey()).isNotNull() ,
                    () -> assertThat(secondMediaFile.getFileType()).isEqualTo(INTERNET_VIDEO_URL),
                    () -> assertThat(secondMediaFile.getVideoStartTime()).isEqualTo("00100"),
                    () -> assertThat(secondMediaFile.getVideoPlayDuration()).isEqualTo(3),
                    () -> assertThat(secondMediaFile.isPlayableVideo()).isEqualTo(true)
            );
        }



        @Test
        @DisplayName(EXCEPTION_PREFIX + "존재하지 않는 월드컵에 컨텐츠를 생성할 수 없다.")
        public void fail1() {

            // given
            var createStaticMediaFileContents = CreateContentsRequest.builder()
                    .contentsName("컨텐츠 이름1")
                    .visibleType(PUBLIC)
                    .createMediaFileRequest(
                            CreateMediaFileRequest.builder()
                                    .fileType(STATIC_MEDIA_FILE)
                                    .mediaData("https://filepaths/1")
                                    .originalName("Original1")
                                    .build()
                    )
                    .build();

            var createInternetVideoUrlContents = CreateContentsRequest.builder()
                    .contentsName("컨텐츠 이름2")
                    .visibleType(PUBLIC)
                    .createMediaFileRequest(
                            CreateMediaFileRequest.builder()
                                    .fileType(INTERNET_VIDEO_URL)
                                    .mediaData("https://filepaths/2")
                                    .videoPlayDuration(3)
                                    .videoStartTime("00100")
                                    .build()
                    )
                    .build();

            var request = builder()
                    .data(List.of(createStaticMediaFileContents, createInternetVideoUrlContents))
                    .build();

            // when
            assertThrows(
                    NotFoundWorldCupGameException.class,
                    () -> worldCupBasedOnAuthService.createMyWorldCupContents(request, 1, 1)
            );

        }


        @Test
        @DisplayName(EXCEPTION_PREFIX + "월드컵의 주인이 아니면 컨텐츠를 생성할 수 없다.")
        public void fail2() {

            // given
            var createStaticMediaFileContents = CreateContentsRequest.builder()
                    .contentsName("컨텐츠 이름1")
                    .visibleType(PUBLIC)
                    .createMediaFileRequest(
                            CreateMediaFileRequest.builder()
                                    .fileType(STATIC_MEDIA_FILE)
                                    .mediaData("https://filepaths/1")
                                    .originalName("Original1")
                                    .build()
                    )
                    .build();

            var createInternetVideoUrlContents = CreateContentsRequest.builder()
                    .contentsName("컨텐츠 이름2")
                    .visibleType(PUBLIC)
                    .createMediaFileRequest(
                            CreateMediaFileRequest.builder()
                                    .fileType(INTERNET_VIDEO_URL)
                                    .mediaData("https://filepaths/2")
                                    .videoPlayDuration(3)
                                    .videoStartTime("00100")
                                    .build()
                    )
                    .build();

            var worldCupGame = WorldCupGame.builder()
                    .title("title1")
                    .visibleType(PUBLIC)
                    .memberId(2)
                    .build();

            worldCupGameRepository.save(worldCupGame);


            var request = builder()
                    .data(List.of(createStaticMediaFileContents, createInternetVideoUrlContents))
                    .build();

            // when
            assertThrows(
                    NotOwnerGameException.class,
                    () -> worldCupBasedOnAuthService.createMyWorldCupContents(request, 1, 1)
            );

        }
    }
}
