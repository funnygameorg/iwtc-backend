package com.example.demo.worldcup.repository;

import com.example.demo.domain.etc.model.MediaFile;
import com.example.demo.domain.etc.repository.MediaFileRepository;
import com.example.demo.domain.worldcup.repository.projection.GetWorldCupGamePageProjection;
import com.example.demo.helper.DataBaseCleanUp;
import com.example.demo.domain.worldcup.repository.WorldCupGameContentsRepository;
import com.example.demo.domain.worldcup.model.WorldCupGame;
import com.example.demo.domain.worldcup.repository.WorldCupGameRepository;
import com.example.demo.domain.worldcup.model.WorldCupGameContents;
import com.example.demo.domain.worldcup.model.vo.WorldCupGameRound;
import com.example.demo.domain.worldcup.model.vo.VisibleType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.IntStream.range;

@ActiveProfiles("test")
@SpringBootTest
public class WorldCupGamePageRepositoryTest {

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

    @Test
    @DisplayName("모든 월드컵 게임, 페이징 조회 성공 - 게임 2개")
    public void 모든_월드컵_게임_페이징_조회_성공() {
        // given
        List<WorldCupGame> worldCupGames = range(1, 3)
                .mapToObj(idx -> createWorldCupGame(
                        "testTitle" + idx,
                        null,
                        WorldCupGameRound.ROUND_16,
                        VisibleType.PRIVATE, idx)
                )
                .toList();

        List<MediaFile> mediaFiles = range(1,7)
                .mapToObj(idx ->
                        createMediaFile("originalName" + idx,
                                "A345ytgs32eff1",
                                "https://s3.dsfwwg4fsesef1/aawr.com",
                                ".png"
                        )
                )
                .toList();

        List<WorldCupGameContents> worldCupGameContentsList1 = range(1, 6)
                .mapToObj(idx -> createGameContents(worldCupGames.get(0), "컨텐츠" + idx, idx))
                .toList();
        List<WorldCupGameContents> worldCupGameContentsList2 = range(5, 7)
                .mapToObj(idx -> createGameContents(worldCupGames.get(1), "컨텐츠" + idx, idx))
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

        assert result.getTotalPages() == 1;
        assert result.getContent().size() == 2;
        assert result.getNumberOfElements() == 2;
        assert result.getNumber() == 0;

        assert firstElement.id() == 2;
        assert Objects.equals(worldCupGames.get(1).getTitle(), firstElement.title());
        assert Objects.equals(worldCupGameContentsList2.get(1).getName(), firstElement.contentsName1());
        assert Objects.equals(worldCupGameContentsList2.get(0).getName(), firstElement.contentsName2());
        assert Objects.equals(mediaFiles.get(5).getFilePath(), firstElement.filePath1());
        assert Objects.equals(mediaFiles.get(4).getFilePath(), firstElement.filePath2());

        assert secondElement.id() == 1;
        assert Objects.equals(worldCupGames.get(0).getTitle(), secondElement.title());
        assert Objects.equals(worldCupGames.get(0).getDescription(), secondElement.description());
        assert Objects.equals(worldCupGameContentsList1.get(4).getName(), secondElement.contentsName1());
        assert Objects.equals(worldCupGameContentsList1.get(3).getName(), secondElement.contentsName2());
        assert Objects.equals(mediaFiles.get(3).getFilePath(), secondElement.filePath1());
        assert Objects.equals(mediaFiles.get(2).getFilePath(), secondElement.filePath2());

    }

    @Test
    @DisplayName("모든 월드컵 게임, 페이징 조회 성공 - 게임 0개")
    public void 모든_월드컵_게임_페이징_조회_성공_컨텐츠_없음() {
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
    @DisplayName("모든 월드컵 게임, 페이징 조회 성공 - 키워드 조건 적용")
    public void 모든_월드컵_게임_페이징_조회_성공_키워드(String worldCupGameKeyword) {
        // given
        WorldCupGame game1 = createWorldCupGame("한국 드라마 월드컵(2000~23.10.04)", "2000년부터 현재까지 한국드라마...", WorldCupGameRound.ROUND_16, VisibleType.PRIVATE, 1);
        WorldCupGame game2 = createWorldCupGame("2022 좋은 노트북 월드컵", "2022년 월드컵 []", WorldCupGameRound.ROUND_4, VisibleType.PRIVATE, 1);

        List<MediaFile> mediaFiles = range(1,7)
                .mapToObj(idx ->
                        createMediaFile("originalName" + idx,
                                "A345ytgs32eff1",
                                "https://s3.dsfwwg4fsesef1/aawr.com",
                                ".png"
                        )
                )
                .toList();

        List<WorldCupGameContents> worldCupGameContentsList = range(1, 7)
                .mapToObj(idx -> createGameContents(game1, "컨텐츠" + idx, idx))
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

        assert result.getTotalPages() == 1;
        assert result.getContent().size() == 1;

        assert firstElement.id() == 1;
        assert Objects.equals(game1.getTitle(), firstElement.title());
        assert Objects.equals(game1.getDescription(), firstElement.description());
        assert Objects.equals(worldCupGameContentsList.get(5).getName(), firstElement.contentsName1());
        assert Objects.equals(worldCupGameContentsList.get(4).getName(), firstElement.contentsName2());
        assert Objects.equals(mediaFiles.get(5).getFilePath(), firstElement.filePath1());
        assert Objects.equals(mediaFiles.get(4).getFilePath(), firstElement.filePath2());

    }

    @ParameterizedTest
    @CsvSource(value = {
            "Warfew",
            "물란",
            "주리주리",
            "교보문고",
            "동국대학교"
    })
    @DisplayName("모든 월드컵 게임, 페이징 조회 실패 - 키워드 조건 적용")
    public void 모든_월드컵_게임_페이징_조회_실패_키워드(String worldCupGameKeyword) {
        // given
        WorldCupGame game1 = createWorldCupGame("한국 드라마 월드컵(2000~23.10.04)", "2000년부터 현재까지 한국드라마...", WorldCupGameRound.ROUND_16, VisibleType.PRIVATE, 1);
        WorldCupGame game2 = createWorldCupGame("2022 좋은 노트북 월드컵", "2022년 월드컵 []", WorldCupGameRound.ROUND_4, VisibleType.PRIVATE, 1);

        List<MediaFile> mediaFiles = range(1,7)
                .mapToObj(idx ->
                        createMediaFile("originalName" + idx,
                                "A345ytgs32eff1",
                                "https://s3.dsfwwg4fsesef1/aawr.com",
                                ".png"
                        )
                )
                .toList();

        List<WorldCupGameContents> worldCupGameContentsList = range(1, 7)
                .mapToObj(idx -> createGameContents(game1, "컨텐츠" + idx, idx))
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
        assert result.getTotalPages() == 1;
        assert result.getContent().size() == 0;
        assert result.getNumberOfElements() == 0;
        assert result.getNumber() == 0;

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
                .round(gameRound)
                .visibleType(visibleType)
                .views(0)
                .softDelete(false)
                .memberId(memberId)
                .build();
    }

    private MediaFile createMediaFile(
            String fileOriginalName,
            String fileAbsoluteName,
            String filePath,
            String extension)
    {
        return MediaFile.builder()
                .originalName(fileOriginalName)
                .absoluteName(fileAbsoluteName)
                .filePath(filePath)
                .extension(extension)
                .build();
    }

    private WorldCupGameContents createGameContents(WorldCupGame game, String name, int mediaFileId) {
        return WorldCupGameContents.builder()
                .name(name)
                .worldCupGame(game)
                .mediaFileId(mediaFileId)
                .build();
    }
}
