package com.example.demo.worldcup.service;

import com.example.demo.TestConstant;
import com.example.demo.domain.etc.model.MediaFile;
import com.example.demo.domain.etc.repository.MediaFileRepository;
import com.example.demo.domain.worldcup.controller.response.GetWorldCupContentsResponse;
import com.example.demo.domain.worldcup.exception.NotOwnerGameException;
import com.example.demo.domain.worldcup.model.WorldCupGame;
import com.example.demo.domain.worldcup.model.WorldCupGameContents;
import com.example.demo.domain.worldcup.model.vo.VisibleType;
import com.example.demo.domain.worldcup.repository.WorldCupGameContentsRepository;
import com.example.demo.domain.worldcup.repository.WorldCupGameRepository;
import com.example.demo.domain.worldcup.service.WorldCupBasedOnAuthService;
import com.example.demo.helper.DataBaseCleanUp;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.demo.TestConstant.EXCEPTION_PREFIX;
import static com.example.demo.TestConstant.SUCCESS_PREFIX;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.*;

@SpringBootTest
@ActiveProfiles("test")
public class WorldCupBasedOnServiceTest {

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
                    .visibleType(VisibleType.PUBLIC)
                    .memberId(1)
                    .build();
            MediaFile mediaFile1 = MediaFile.builder()
                    .originalName("fileOriginalName")
                    .absoluteName("fileAbsoluteName")
                    .filePath("https://www.abc.com/BS/1")
                    .extension("extension")
                    .build();
            MediaFile mediaFile2 = MediaFile.builder()
                    .originalName("fileOriginalName")
                    .absoluteName("fileAbsoluteName")
                    .filePath("https://www.abc.com/BS/2")
                    .extension("extension")
                    .build();
            WorldCupGameContents contents1 = WorldCupGameContents.builder()
                    .name("컨텐츠1")
                    .gameScore(10)
                    .gameRank(1)
                    .visibleType(VisibleType.PUBLIC)
                    .worldCupGame(worldCupGame)
                    .mediaFile(mediaFile1)
                    .build();
            WorldCupGameContents contents2  = WorldCupGameContents.builder()
                    .name("컨텐츠2")
                    .gameScore(5)
                    .gameRank(2)
                    .visibleType(VisibleType.PUBLIC)
                    .worldCupGame(worldCupGame)
                    .mediaFile(mediaFile2)
                    .build();
            worldCupGameRepository.save(worldCupGame);
            mediaFileRepository.saveAll(List.of(mediaFile1, mediaFile2));
            worldCupGameContentsRepository.saveAll(List.of(contents1, contents2));

            long worldCupId = 1;
            long memberId = 1;

            // when
            List<GetWorldCupContentsResponse> response = worldCupBasedOnAuthService.getMyWorldCupGameContents (worldCupId, memberId);

            // then
            assertThat(response.size(), is(2));

            GetWorldCupContentsResponse firstElement = response.get(0);
            GetWorldCupContentsResponse secondElement = response.get(1);

            assertThat(firstElement.contentsId(), is(1L));
            assertThat(firstElement.contentsName(), is("컨텐츠1"));
            assertThat(firstElement.rank(), is(1));
            assertThat(firstElement.score(), is(10));
            assertThat(firstElement.fileResponse().mediaFileId(), is(1L));
            assertThat(firstElement.fileResponse().filePath(), is("https://www.abc.com/BS/1"));
            assertThat(firstElement.fileResponse().createdAt(), is(any(LocalDateTime.class)));
            assertThat(firstElement.fileResponse().updatedAt(), is(any(LocalDateTime.class)));

            assertThat(secondElement.contentsId(), is(2L));
            assertThat(secondElement.contentsName(), is("컨텐츠2"));
            assertThat(secondElement.rank(), is(2));
            assertThat(secondElement.score(), is(5));
            assertThat(secondElement.fileResponse().mediaFileId(), is(2L));
            assertThat(secondElement.fileResponse().filePath(), is("https://www.abc.com/BS/2"));
            assertThat(secondElement.fileResponse().createdAt(), is(is(any(LocalDateTime.class))));
            assertThat(secondElement.fileResponse().updatedAt(), is(is(any(LocalDateTime.class))));
        }

        @Test
        @DisplayName(EXCEPTION_PREFIX + "월드컵 게임 작성자가 아닌 것은 조회할 수 없다.")
        public void fail2() {
            // given
            WorldCupGame worldCupGame = WorldCupGame.builder()
                    .title("게임1")
                    .description("설명1")
                    .visibleType(VisibleType.PUBLIC)
                    .memberId(2)
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
                    .name("컨텐츠1")
                    .gameScore(1)
                    .visibleType(VisibleType.PUBLIC)
                    .worldCupGame(worldCupGame)
                    .mediaFile(mediaFile1)
                    .build();
            WorldCupGameContents contents2  = WorldCupGameContents.builder()
                    .name("컨텐츠2")
                    .gameScore(1)
                    .visibleType(VisibleType.PUBLIC)
                    .worldCupGame(worldCupGame)
                    .mediaFile(mediaFile2)
                    .build();
            worldCupGameRepository.save(worldCupGame);
            mediaFileRepository.saveAll(List.of(mediaFile1, mediaFile2));
            worldCupGameContentsRepository.saveAll(List.of(contents1, contents2));

            long worldCupId = 1;
            long memberId = 1;

            // when
            org.junit.jupiter.api.Assertions.assertThrows(
                    NotOwnerGameException.class,
                    ()-> worldCupBasedOnAuthService.getMyWorldCupGameContents(worldCupId, memberId)
            );

        }

    }
}
