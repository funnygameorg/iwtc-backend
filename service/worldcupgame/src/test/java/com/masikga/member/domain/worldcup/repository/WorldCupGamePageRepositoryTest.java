package com.masikga.member.domain.worldcup.repository;

import com.masikga.member.helper.DataBaseCleanUp;
import com.masikga.member.helper.testbase.IntegrationBaseTest;
import com.masikga.worldcupgame.domain.etc.model.StaticMediaFile;
import com.masikga.worldcupgame.domain.etc.repository.MediaFileRepository;
import com.masikga.worldcupgame.domain.worldcup.model.WorldCupGame;
import com.masikga.worldcupgame.domain.worldcup.model.WorldCupGameContents;
import com.masikga.worldcupgame.domain.worldcup.model.vo.VisibleType;
import com.masikga.worldcupgame.domain.worldcup.model.vo.WorldCupGameRound;
import com.masikga.worldcupgame.domain.worldcup.repository.WorldCupGameContentsRepository;
import com.masikga.worldcupgame.domain.worldcup.repository.WorldCupGameRepository;
import com.masikga.worldcupgame.domain.worldcup.repository.projection.GetWorldCupGamePageProjection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static com.masikga.member.helper.TestConstant.SUCCESS_PREFIX;
import static com.masikga.worldcupgame.domain.worldcup.model.vo.VisibleType.PRIVATE;
import static com.masikga.worldcupgame.domain.worldcup.model.vo.VisibleType.PUBLIC;
import static com.masikga.worldcupgame.domain.worldcup.model.vo.WorldCupGameRound.ROUND_16;
import static com.masikga.worldcupgame.domain.worldcup.model.vo.WorldCupGameRound.ROUND_4;
import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
                            ROUND_16,
                            PUBLIC, idx)
                    )
                    .toList();

            List<StaticMediaFile> mediaFiles = range(1, 8)
                    .mapToObj(idx ->
                            createMediaFile("originalName" + idx,
                                    "A345ytgs32eff1",
                                    "https://s3.dsfwwg4fsesef1/aawr.com",
                                    "JPG"
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
                    pageable,
                    null
            );

            // then
            GetWorldCupGamePageProjection firstElement = result.getContent().get(0);
            GetWorldCupGamePageProjection secondElement = result.getContent().get(1);

            assertAll(
                    () -> assertThat(result.getTotalPages()).isEqualTo(1),
                    () -> assertThat(result.getContent().size()).isEqualTo(2),
                    () -> assertThat(result.getNumberOfElements()).isEqualTo(2),
                    () -> assertThat(result.getNumber()).isEqualTo(0),

                    () -> assertThat(firstElement.worldCupId()).isEqualTo(2),
                    () -> assertThat(firstElement.title()).isEqualTo(worldCupGames.get(1).getTitle()),
                    () -> assertThat(firstElement.contentsName1()).isEqualTo("컨텐츠7"),
                    () -> assertThat(firstElement.contentsName2()).isEqualTo("컨텐츠6"),
                    () -> assertThat(firstElement.mediaFileId1()).isEqualTo(7),
                    () -> assertThat(firstElement.mediaFileId2()).isEqualTo(6),

                    () -> assertThat(secondElement.worldCupId()).isEqualTo(1),
                    () -> assertThat(secondElement.title()).isEqualTo(worldCupGames.get(0).getTitle()),
                    () -> assertThat(secondElement.description()).isEqualTo(worldCupGames.get(0).getDescription()),
                    () -> assertThat(secondElement.contentsName1()).isEqualTo("컨텐츠5"),
                    () -> assertThat(secondElement.contentsName2()).isEqualTo("컨텐츠4"),
                    () -> assertThat(secondElement.mediaFileId1()).isEqualTo(5),
                    () -> assertThat(secondElement.mediaFileId2()).isEqualTo(4)
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
                    pageable,
                    null
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
            WorldCupGame game1 = createWorldCupGame(
                    "한국 드라마 월드컵(2000~23.10.04)",
                    "2000년부터 현재까지 한국드라마...",
                    ROUND_16,
                    PUBLIC,
                    1
            );
            WorldCupGame game2 = createWorldCupGame(
                    "2022 좋은 노트북 월드컵",
                    "2022년 월드컵 []",
                    ROUND_4,
                    PUBLIC,
                    1
            );

            List<StaticMediaFile> mediaFiles = range(1, 7)
                    .mapToObj(idx ->
                            createMediaFile("originalName" + idx,
                                    "A345ytgs32eff1",
                                    "https://s3.dsfwwg4fsesef1/aawr.com",
                                    "JPEG"
                            )
                    )
                    .toList();

            List<WorldCupGameContents> worldCupGameContentsList = range(1, 7)
                    .mapToObj(idx -> createGameContents(
                            game1,
                            "컨텐츠" + idx,
                            mediaFiles.get(idx - 1))
                    )
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
                    pageable,
                    null
            );

            // then
            GetWorldCupGamePageProjection firstElement = result.getContent().get(0);

            assertAll(
                    () -> assertThat(result.getTotalPages()).isEqualTo(1),
                    () -> assertThat(result.getContent().size()).isEqualTo(1),

                    () -> assertThat(firstElement.worldCupId()).isEqualTo(1),
                    () -> assertThat(firstElement.title()).isEqualTo(game1.getTitle()),
                    () -> assertThat(firstElement.description()).isEqualTo(game1.getDescription()),
                    () -> assertThat(firstElement.contentsName1()).isEqualTo(worldCupGameContentsList.get(5).getName()),
                    () -> assertThat(firstElement.contentsName2()).isEqualTo(worldCupGameContentsList.get(4).getName()),
                    () -> assertThat(firstElement.mediaFileId1()).isEqualTo(6),
                    () -> assertThat(firstElement.mediaFileId2()).isEqualTo(5)
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
            WorldCupGame game1 = createWorldCupGame("한국 드라마 월드컵(2000~23.10.04)", "2000년부터 현재까지 한국드라마...", ROUND_16,
                    PUBLIC, 1);
            WorldCupGame game2 = createWorldCupGame("2022 좋은 노트북 월드컵", "2022년 월드컵 []", ROUND_4, PUBLIC, 1);

            List<StaticMediaFile> mediaFiles = range(1, 7)
                    .mapToObj(idx ->
                            createMediaFile("originalName" + idx,
                                    "A345ytgs32eff1",
                                    "https://s3.dsfwwg4fsesef1/aawr.com",
                                    "MP4"
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
                    pageable,
                    null
            );

            // then
            assertAll(
                    () -> assertThat(result.getTotalPages()).isEqualTo(1),
                    () -> assertThat(result.getContent().size()).isEqualTo(0),
                    () -> assertThat(result.getNumberOfElements()).isEqualTo(0),
                    () -> assertThat(result.getNumber()).isEqualTo(0)
            );
        }

        @ParameterizedTest
        @MethodSource("getMemberIdAndMembersGameSize")
        @DisplayName(SUCCESS_PREFIX + "멤버 ID 적용")
        public void success4(long memberId, long expectedGameSize) {
            // given
            WorldCupGame game1 = createWorldCupGame(
                    "한국 드라마 월드컵(2000~23.10.04)",
                    "2000년부터 현재까지 한국드라마...",
                    ROUND_16,
                    PUBLIC,
                    1
            );
            WorldCupGame game2 = createWorldCupGame(
                    "2022 좋은 노트북 월드컵",
                    "2022년 월드컵 []",
                    ROUND_4,
                    PUBLIC,
                    3
            );

            List<StaticMediaFile> mediaFiles = range(1, 7)
                    .mapToObj(idx ->
                            createMediaFile("originalName" + idx,
                                    "A345ytgs32eff1",
                                    "https://s3.dsfwwg4fsesef1/aawr.com",
                                    "MP4"
                            )
                    )
                    .toList();

            List<WorldCupGameContents> worldCupGameContentsList = range(1, 7)
                    .mapToObj(idx -> createGameContents(
                            game1,
                            "컨텐츠" + idx,
                            mediaFiles.get(idx - 1))
                    )
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
                    null,
                    pageable,
                    memberId
            );

            // then
            assertThat(result.getContent().size()).isEqualTo(expectedGameSize);
        }

        public static Stream<Arguments> getMemberIdAndMembersGameSize() {
            return Stream.of(
                    Arguments.of(1, 1),
                    Arguments.of(2, 0)
            );
        }

    }

    @Test
    @DisplayName(SUCCESS_PREFIX + "'PUBLIC 상태인 게임만 조회 가능'조건으로 게임 3개에서 1개 조회")
    public void success1() {
        // given
        List<WorldCupGame> privateGames = range(1, 3)
                .mapToObj(idx -> createWorldCupGame(
                        "testTitle" + idx,
                        null,
                        ROUND_16,
                        PRIVATE, idx)
                )
                .toList();

        WorldCupGame publicGame = createWorldCupGame("hi game!", "hi description!", ROUND_16, PUBLIC, 1);

        List<StaticMediaFile> mediaFiles = range(1, 8)
                .mapToObj(idx ->
                        createMediaFile("originalName" + idx,
                                "A345ytgs32eff1",
                                "https://s3.dsfwwg4fsesef1/aawr.com",
                                "PNG"
                        )
                )
                .toList();

        List<StaticMediaFile> publicMediaFiles = range(1, 3)
                .mapToObj(idx ->
                        createMediaFile("originalName" + idx,
                                "absolute",
                                "https://s3.public/aawr.com",
                                "GIF"
                        )
                )
                .toList();

        List<WorldCupGameContents> worldCupGameContentsList1 = range(1, 6)
                .mapToObj(idx -> createGameContents(privateGames.get(0), "컨텐츠" + idx, mediaFiles.get(idx - 1)))
                .toList();
        List<WorldCupGameContents> worldCupGameContentsList2 = range(6, 8)
                .mapToObj(idx -> createGameContents(privateGames.get(1), "컨텐츠" + idx, mediaFiles.get(idx - 1)))
                .toList();
        List<WorldCupGameContents> publicGameContentsList = range(1, 3)
                .mapToObj(idx -> createGameContents(publicGame, "퍼블릭 컨텐츠" + idx, publicMediaFiles.get(idx - 1)))
                .toList();

        worldCupGameRepository.saveAll(privateGames);
        mediaFileRepository.saveAll(mediaFiles);
        worldCupGameContentsRepository.saveAll(worldCupGameContentsList1);
        worldCupGameContentsRepository.saveAll(worldCupGameContentsList2);

        worldCupGameRepository.save(publicGame);
        mediaFileRepository.saveAll(publicMediaFiles);
        worldCupGameContentsRepository.saveAll(publicGameContentsList);

        LocalDate startDate = LocalDate.now().minusDays(2);
        LocalDate endDate = LocalDate.now();
        Pageable pageable = PageRequest.of(0, 25, Sort.Direction.DESC, "id");

        // when
        Page<GetWorldCupGamePageProjection> result = worldCupGameRepository.getWorldCupGamePage(
                startDate,
                endDate,
                null,
                pageable,
                null
        );

        // then
        GetWorldCupGamePageProjection firstElement = result.getContent().get(0);

        assertAll(
                () -> assertThat(result.getTotalPages()).isEqualTo(1),
                () -> assertThat(result.getContent().size()).isEqualTo(1),
                () -> assertThat(result.getNumberOfElements()).isEqualTo(1),
                () -> assertThat(result.getNumber()).isEqualTo(0),

                () -> assertThat(firstElement.worldCupId()).isEqualTo(3),
                () -> assertThat(firstElement.title()).isEqualTo("hi game!"),
                () -> assertThat(firstElement.contentsName1()).isEqualTo("퍼블릭 컨텐츠2"),
                () -> assertThat(firstElement.contentsName2()).isEqualTo("퍼블릭 컨텐츠1"),
                () -> assertThat(firstElement.mediaFileId1()).isEqualTo(9),
                () -> assertThat(firstElement.mediaFileId2()).isEqualTo(8)

        );

    }

    private WorldCupGame createWorldCupGame(
            String title,
            String description,
            WorldCupGameRound gameRound,
            VisibleType visibleType,
            int memberId) {
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
            String extension) {
        return StaticMediaFile.builder()
                .originalName(fileOriginalName)
                .objectKey(filePath)
                .extension(extension)
                .originalFileSize("0")
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
