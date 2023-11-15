package com.example.demo.worldcup.service;

import com.example.demo.domain.etc.model.StaticMediaFile;
import com.example.demo.domain.etc.repository.MediaFileRepository;
import com.example.demo.domain.worldcup.controller.request.CreateWorldCupRequest;
import com.example.demo.domain.worldcup.controller.response.GetWorldCupContentsResponse;
import com.example.demo.domain.worldcup.exception.DuplicatedWorldCupGameTitleException;
import com.example.demo.domain.worldcup.exception.NotFoundWorldCupGameException;
import com.example.demo.domain.worldcup.exception.NotOwnerGameException;
import com.example.demo.domain.worldcup.model.WorldCupGame;
import com.example.demo.domain.worldcup.model.WorldCupGameContents;
import com.example.demo.domain.worldcup.repository.WorldCupGameContentsRepository;
import com.example.demo.domain.worldcup.repository.WorldCupGameRepository;
import com.example.demo.domain.worldcup.service.WorldCupBasedOnAuthService;
import com.example.demo.helper.DataBaseCleanUp;
import com.example.demo.helper.testbase.IntegrationBaseTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.demo.domain.worldcup.model.vo.VisibleType.PRIVATE;
import static com.example.demo.domain.worldcup.model.vo.VisibleType.PUBLIC;
import static com.example.demo.helper.TestConstant.EXCEPTION_PREFIX;
import static com.example.demo.helper.TestConstant.SUCCESS_PREFIX;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WorldCupBasedOnServiceTest implements IntegrationBaseTest {

    @Autowired
    private WorldCupBasedOnAuthService worldCupBasedOnAuthService;
    @Autowired
    private WorldCupGameRepository worldCupGameRepository;
    @Autowired
    private MediaFileRepository mediaFileRepository;
    @Autowired
    private WorldCupGameContentsRepository worldCupGameContentsRepository;
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
                    .absoluteName("fileAbsoluteName")
                    .filePath("https://www.abc.com/BS/1")
                    .extension("extension")
                    .build();
            StaticMediaFile mediaFile2 = StaticMediaFile.builder()
                    .originalName("fileOriginalName")
                    .absoluteName("fileAbsoluteName")
                    .filePath("https://www.abc.com/BS/2")
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
            List<WorldCupGameContents> ss = worldCupGameContentsRepository.findAllByWorldCupGame(worldCupGame);

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
}
