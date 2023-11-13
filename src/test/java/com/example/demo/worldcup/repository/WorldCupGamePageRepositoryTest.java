package com.example.demo.worldcup.repository;

import com.example.demo.domain.etc.model.StaticMediaFile;
import com.example.demo.domain.etc.repository.MediaFileRepository;
import com.example.demo.domain.worldcup.repository.projection.GetWorldCupGamePageProjection;
import com.example.demo.helper.DataBaseCleanUp;
import com.example.demo.domain.worldcup.repository.WorldCupGameContentsRepository;
import com.example.demo.domain.worldcup.model.WorldCupGame;
import com.example.demo.domain.worldcup.repository.WorldCupGameRepository;
import com.example.demo.domain.worldcup.model.WorldCupGameContents;
import com.example.demo.domain.worldcup.model.vo.WorldCupGameRound;
import com.example.demo.domain.worldcup.model.vo.VisibleType;
import com.example.demo.helper.testbase.IntegrationBaseTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

import static com.example.demo.helper.TestConstant.SUCCESS_PREFIX;
import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class WorldCupGamePageRepositoryTest implements IntegrationBaseTest {

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
    @DisplayName("월드컵 게임 페이지를 조회할 수 있다.")
    public class getWorldCupGamePage {

        @Test
        @DisplayName(SUCCESS_PREFIX + "게임 2개 조회")
        public void success1() {
            // given
            List<WorldCupGame> worldCupGames = range(1, 3)
                    .mapToObj(idx -> createWorldCupGame(
                            "testTitle" + idx,
                            null,
                            WorldCupGameRound.ROUND_16,
                            VisibleType.PRIVATE, idx)
                    )
                    .toList();

            List<StaticMediaFile> mediaFiles = range(1,8)
                    .mapToObj(idx ->
                            createMediaFile("originalName" + idx,
                                    "A345ytgs32eff1",
                                    "https://s3.dsfwwg4fsesef1/aawr.com",
                                    ".png"
                            )
                    )
                    .toList();

            List<WorldCupGameContents> worldCupGameContentsList1 = range(1, 6)
                    .mapToObj(idx -> createGameContents(worldCupGames.get(0), "컨텐츠" + idx, mediaFiles.get(idx - 1)))
                    .toList();
            List<WorldCupGameContents> worldCupGameContentsList2 = range(6, 8)
                    .mapToObj(idx -> createGameContents(worldCupGames.get(1), "컨텐츠" + idx, mediaFiles.get(idx - 1)))
                    .toList();

            worldCupGameRepository.saveAll(worldCupGames);
            mediaFileRepository.saveAll(mediaFiles);
            worldCupGameContentsRepository.saveAll(worldCupGameContentsList1);
            worldCupGameContentsRepository.saveAll(worldCupGameContentsList2);

            String worldCupGameKeyword = "test";
            LocalDate startDate = LocalDate.now().minusDays(2);
            LocalDate endDate = LocalDate.now();
            Pageable pageable = PageRequest.of(0, 25, Sort.Direction.DESC, "id");

            // when
            Page<GetWorldCupGamePageProjection> result = worldCupGameRepository.getWorldCupGamePage(
                    startDate,
                    endDate,
                    worldCupGameKeyword,
                    pageable
            );

            // then
            GetWorldCupGamePageProjection firstElement = result.getContent().get(0);
            GetWorldCupGamePageProjection secondElement = result.getContent().get(1);

            assertAll(
                    () -> assertThat(result.getTotalPages()).isEqualTo(1),
                    () -> assertThat(result.getContent().size()).isEqualTo(2),
                    () -> assertThat(result.getNumberOfElements()).isEqualTo(2),
                    () -> assertThat(result.getNumber()).isEqualTo(0),

                    () -> assertThat(firstElement.id()).isEqualTo(2),
                    () -> assertThat(firstElement.title()).isEqualTo(worldCupGames.get(1).getTitle()),
                    () -> assertThat(firstElement.contentsName1()).isEqualTo(worldCupGameContentsList2.get(1).getName()),
                    () -> assertThat(firstElement.contentsName2()).isEqualTo(worldCupGameContentsList2.get(0).getName()),
                    () -> assertThat(firstElement.filePath1()).isEqualTo(mediaFiles.get(5).getFilePath()),
                    () -> assertThat(firstElement.filePath2()).isEqualTo(mediaFiles.get(4).getFilePath()),

                    () -> assertThat(secondElement.id()).isEqualTo(1),
                    () -> assertThat(secondElement.title()).isEqualTo(worldCupGames.get(0).getTitle()),
                    () -> assertThat(secondElement.description()).isEqualTo(worldCupGames.get(0).getDescription()),
                    () -> assertThat(secondElement.contentsName1()).isEqualTo(worldCupGameContentsList1.get(4).getName()),
                    () -> assertThat(secondElement.contentsName2()).isEqualTo(worldCupGameContentsList1.get(3).getName()),
                    () -> assertThat(secondElement.filePath1()).isEqualTo(mediaFiles.get(3).getFilePath()),
                    () -> assertThat(secondElement.filePath2()).isEqualTo(mediaFiles.get(2).getFilePath())
            );

        }

        @Test
        @DisplayName(SUCCESS_PREFIX + "게임 0개 조회")
        public void success2() {
            // given
            String worldCupGameKeyword = "test";
            LocalDate startDate = LocalDate.now().minusDays(2);
            LocalDate endDate = LocalDate.now();
            Pageable pageable = PageRequest.of(0, 25, Sort.Direction.DESC, "id");

            // when
            Page<GetWorldCupGamePageProjection> result = worldCupGameRepository.getWorldCupGamePage(
                    startDate,
                    endDate,
                    worldCupGameKeyword,
                    pageable
            );

            // then
            assert result.getTotalPages() == 0;
            assert result.getContent().size() == 0;
            assert result.getNumberOfElements() == 0;
            assert result.getNumber() == 0;
        }


        @ParameterizedTest
        @CsvSource(value = {
                "한국드라마",
                "한국  드라마",
                "   한국  드라마   ",
                "한 국 드 라 마",
        })
        @DisplayName(SUCCESS_PREFIX + "게임 1개 조회, 키워드 적용")
        public void success3(String worldCupGameKeyword) {
            // given
            WorldCupGame game1 = createWorldCupGame("한국 드라마 월드컵(2000~23.10.04)", "2000년부터 현재까지 한국드라마...", WorldCupGameRound.ROUND_16, VisibleType.PRIVATE, 1);
            WorldCupGame game2 = createWorldCupGame("2022 좋은 노트북 월드컵", "2022년 월드컵 []", WorldCupGameRound.ROUND_4, VisibleType.PRIVATE, 1);

            List<StaticMediaFile> mediaFiles = range(1,7)
                    .mapToObj(idx ->
                            createMediaFile("originalName" + idx,
                                    "A345ytgs32eff1",
                                    "https://s3.dsfwwg4fsesef1/aawr.com",
                                    ".png"
                            )
                    )
                    .toList();

            List<WorldCupGameContents> worldCupGameContentsList = range(1, 7)
                    .mapToObj(idx -> createGameContents(game1, "컨텐츠" + idx, mediaFiles.get(idx - 1)))
                    .toList();

            worldCupGameRepository.saveAll(List.of(game1, game2));
            mediaFileRepository.saveAll(mediaFiles);
            worldCupGameContentsRepository.saveAll(worldCupGameContentsList);

            LocalDate startDate = LocalDate.now().minusDays(2);
            LocalDate endDate = LocalDate.now();
            Pageable pageable = PageRequest.of(0, 25, Sort.Direction.DESC, "id");

            // when
            Page<GetWorldCupGamePageProjection> result = worldCupGameRepository.getWorldCupGamePage(
                    startDate,
                    endDate,
                    worldCupGameKeyword,
                    pageable
            );

            // then
            GetWorldCupGamePageProjection firstElement = result.getContent().get(0);

            assertAll(
                    () -> assertThat(result.getTotalPages()).isEqualTo(1),
                    () -> assertThat(result.getContent().size()).isEqualTo(1),

                    () -> assertThat(firstElement.id()).isEqualTo(1),
                    () -> assertThat(firstElement.title()).isEqualTo(game1.getTitle()),
                    () -> assertThat(firstElement.description()).isEqualTo(game1.getDescription()),
                    () -> assertThat(firstElement.contentsName1()).isEqualTo(worldCupGameContentsList.get(5).getName()),
                    () -> assertThat(firstElement.contentsName2()).isEqualTo(worldCupGameContentsList.get(4).getName()),
                    () -> assertThat(firstElement.filePath1()).isEqualTo(mediaFiles.get(5).getFilePath()),
                    () -> assertThat(firstElement.filePath2()).isEqualTo(mediaFiles.get(4).getFilePath())
            );

        }

        @ParameterizedTest
        @CsvSource(value = {
                "Warfew",
                "물란",
                "주리주리",
                "교보문고",
                "동국대학교"
        })
        @DisplayName(SUCCESS_PREFIX + "게임 0개 조회, 키워드 적용")
        public void success4(String worldCupGameKeyword) {
            // given
            WorldCupGame game1 = createWorldCupGame("한국 드라마 월드컵(2000~23.10.04)", "2000년부터 현재까지 한국드라마...", WorldCupGameRound.ROUND_16, VisibleType.PRIVATE, 1);
            WorldCupGame game2 = createWorldCupGame("2022 좋은 노트북 월드컵", "2022년 월드컵 []", WorldCupGameRound.ROUND_4, VisibleType.PRIVATE, 1);

            List<StaticMediaFile> mediaFiles = range(1,7)
                    .mapToObj(idx ->
                            createMediaFile("originalName" + idx,
                                    "A345ytgs32eff1",
                                    "https://s3.dsfwwg4fsesef1/aawr.com",
                                    ".png"
                            )
                    )
                    .toList();

            List<WorldCupGameContents> worldCupGameContentsList = range(1, 7)
                    .mapToObj(idx -> createGameContents(game1, "컨텐츠" + idx, mediaFiles.get(idx - 1)))
                    .toList();

            worldCupGameRepository.saveAll(List.of(game1, game2));
            mediaFileRepository.saveAll(mediaFiles);
            worldCupGameContentsRepository.saveAll(worldCupGameContentsList);

            LocalDate startDate = LocalDate.now().minusDays(2);
            LocalDate endDate = LocalDate.now();
            Pageable pageable = PageRequest.of(0, 25, Sort.Direction.DESC, "id");

            // when
            Page<GetWorldCupGamePageProjection> result = worldCupGameRepository.getWorldCupGamePage(
                    startDate,
                    endDate,
                    worldCupGameKeyword,
                    pageable
            );

            // then
            assertAll(
                    () -> assertThat(result.getTotalPages()).isEqualTo(1),
                    () -> assertThat(result.getContent().size()).isEqualTo(0),
                    () -> assertThat(result.getNumberOfElements()).isEqualTo(0),
                    () -> assertThat(result.getNumber()).isEqualTo(0)
            );
        }

    }


    private WorldCupGame createWorldCupGame(
            String title,
            String description,
            WorldCupGameRound gameRound,
            VisibleType visibleType,
            int memberId)
    {
        return WorldCupGame.builder()
                .title(title)
                .description(description)
                .visibleType(visibleType)
                .views(0)
                .softDelete(false)
                .memberId(memberId)
                .build();
    }

    private StaticMediaFile createMediaFile(
            String fileOriginalName,
            String fileAbsoluteName,
            String filePath,
            String extension)
    {
        return StaticMediaFile.builder()
                .originalName(fileOriginalName)
                .absoluteName(fileAbsoluteName)
                .filePath(filePath)
                .extension(extension)
                .build();
    }

    private WorldCupGameContents createGameContents(WorldCupGame game, String name, StaticMediaFile mediaFile) {
        return WorldCupGameContents.builder()
                .name(name)
                .worldCupGame(game)
                .mediaFile(mediaFile)
                .build();
    }
}
