package com.example.demo.worldcup.service;

import com.example.demo.TestConstant;
import com.example.demo.domain.etc.model.MediaFile;
import com.example.demo.domain.etc.repository.MediaFileRepository;
import com.example.demo.domain.worldcup.model.WorldCupGame;
import com.example.demo.domain.worldcup.model.WorldCupGameContents;
import com.example.demo.domain.worldcup.controller.vo.WorldCupDateRange;
import com.example.demo.domain.worldcup.repository.WorldCupGameContentsRepository;
import com.example.demo.domain.worldcup.repository.WorldCupGameRepository;
import com.example.demo.domain.worldcup.repository.projection.GetWorldCupGamePageProjection;
import com.example.demo.domain.worldcup.service.WorldCupGameService;
import com.example.demo.helper.DataBaseCleanUp;
import com.example.demo.helper.testbase.IntegrationBaseTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.example.demo.TestConstant.SUCCESS_PREFIX;
import static com.example.demo.domain.worldcup.model.vo.VisibleType.PUBLIC;

public class WorldCupGameServiceTest implements IntegrationBaseTest {

    @Autowired
    private WorldCupGameService worldCupGameService;
    @Autowired
    private WorldCupGameRepository worldCupGameRepository;
    @Autowired
    private WorldCupGameContentsRepository worldCupGameContentsRepository;
    @Autowired
    private MediaFileRepository mediaFileRepository;
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
        public void find1() {
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
                    .name("contentsName2")
                    .worldCupGame(worldCupGame)
                    .mediaFile(mediaFile2)
                    .build();

            worldCupGameRepository.save(worldCupGame);
            mediaFileRepository.saveAll(List.of(mediaFile1, mediaFile2));
            worldCupGameContentsRepository.saveAll(List.of(contents1, contents2));

            WorldCupDateRange dateRange = WorldCupDateRange.YEAR;
            String worldCupGameKeyword = null;
            Pageable pageable = PageRequest.of(0, 25, Sort.Direction.DESC, "id");

            // when
            Page<GetWorldCupGamePageProjection> result = worldCupGameService.findWorldCupByPageable(
                    pageable,
                    dateRange,
                    worldCupGameKeyword
            );

            // then
            assert result.getContent().size() == 1;
        }

        @Test
        @DisplayName(SUCCESS_PREFIX + "2개 조회")
        public void find2() {
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
            mediaFileRepository.saveAll(List.of(mediaFile1, mediaFile2, mediaFile3, mediaFile4));
            worldCupGameContentsRepository.saveAll(List.of(contents1, contents2, contents3, contents4));

            WorldCupDateRange dateRange = WorldCupDateRange.ALL;
            String worldCupGameKeyword = null;
            Pageable pageable = PageRequest.of(0, 25, Sort.Direction.DESC, "id");

            // when
            Page<GetWorldCupGamePageProjection> result = worldCupGameService.findWorldCupByPageable(
                    pageable,
                    dateRange,
                    worldCupGameKeyword
            );

            // then
            assert result.getContent().size() == 2;
        }

        @Test
        @Disabled
        @DisplayName(SUCCESS_PREFIX + "1개 조회, 컨텐츠 2개 미만 게임은 조회대상에서 제외")
        public void find1_exclude2() {
            // 해당 정책 테스트 코드 추가하기
            assert false;
        }
    }

}
