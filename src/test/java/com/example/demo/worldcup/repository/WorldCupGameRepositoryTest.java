package com.example.demo.worldcup.repository;

import com.example.demo.domain.etc.model.MediaFile;
import com.example.demo.domain.etc.model.MediaFileRepository;
import com.example.demo.domain.worldcup.model.projection.GetWorldCupGamePageProjection;
import com.example.demo.helper.DataBaseCleanUp;
import com.example.demo.domain.worldcup.model.repository.WorldCupGameContentsRepository;
import com.example.demo.domain.worldcup.model.entity.WorldCupGame;
import com.example.demo.domain.worldcup.model.repository.WorldCupGameRepository;
import com.example.demo.domain.worldcup.model.entity.WorldCupGameContents;
import com.example.demo.domain.worldcup.model.entity.vo.WorldCupGameRound;
import com.example.demo.domain.worldcup.model.entity.vo.VisibleType;
import net.bytebuddy.implementation.auxiliary.MethodCallProxy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
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

@ActiveProfiles("test")
@SpringBootTest
public class WorldCupGameRepositoryTest {

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
        WorldCupGame game1 = createWorldCupGame("testTitle1", null, WorldCupGameRound.ROUND_16, VisibleType.PRIVATE, 1);
        WorldCupGame game2 = createWorldCupGame("testTitle2", "", WorldCupGameRound.ROUND_4, VisibleType.PRIVATE, 1);

        MediaFile mediaFile1 = createMediaFile("originalName1", "A345ytgs32eff1", "https://s3.dsfwwg4fsesef1/aawr.com", ".png");
        MediaFile mediaFile2 = createMediaFile("originalName2", "A345ytgs32eff2", "https://s3.dsfwwg4fsesef2/aawr.com", ".png");
        MediaFile mediaFile3 = createMediaFile("originalName3", "A345ytgs32eff3", "https://s3.dsfwwg4fsesef3/aawr.com", ".png");
        MediaFile mediaFile4 = createMediaFile("originalName4", "A345ytgs32eff4", "https://s3.dsfwwg4fsesef4/aawr.com", ".png");
        MediaFile mediaFile5 = createMediaFile("originalName5", "A345ytgs32eff5", "https://s3.dsfwwg4fsesef5/aawr.com", ".png");
        MediaFile mediaFile6 = createMediaFile("originalName6", "A345ytgs32eff6", "https://s3.dsfwwg4fsesef6/aawr.com", ".jpg");

        WorldCupGameContents contents1 = createGameContents(game1, "컨텐츠1", 1);
        WorldCupGameContents contents2 = createGameContents(game1, "컨텐츠2", 2);
        WorldCupGameContents contents3 = createGameContents(game1, "컨텐츠3", 3);
        WorldCupGameContents contents4 = createGameContents(game1, "컨텐츠4", 4);
        WorldCupGameContents contents5 = createGameContents(game2, "컨텐츠5", 5);
        WorldCupGameContents contents6 = createGameContents(game2, "컨텐츠6", 6);

        worldCupGameRepository.saveAll(List.of(game1, game2));
        mediaFileRepository.saveAll(List.of(mediaFile1, mediaFile2, mediaFile3, mediaFile4, mediaFile5, mediaFile6));
        worldCupGameContentsRepository.saveAll(List.of(contents1, contents2, contents3, contents4, contents5, contents6));

        String worldCupGameKeyword = "test";
        LocalDate startDate = LocalDate.now().minusDays(2);
        LocalDate endDate = LocalDate.now();
        Pageable pageable = PageRequest.of(0, 25, Sort.Direction.DESC, "id");

        Page<GetWorldCupGamePageProjection> result = worldCupGameRepository.getWorldCupGamePage(
                startDate,
                endDate,
                worldCupGameKeyword,
                pageable
        );

        result.getContent().forEach(it ->
                System.out.println(it.id() + ", " + it.contentsName1() + ", " + it.contentsName2())
        );

        GetWorldCupGamePageProjection firstElement = result.getContent().get(0);
        GetWorldCupGamePageProjection secondElement = result.getContent().get(1);

        assert result.getTotalPages() == 1;
        assert result.getContent().size() == 2;
        assert result.getNumberOfElements() == 2;
        assert result.getNumber() == 0;

        assert firstElement.id() == 2;
        assert Objects.equals(game2.getTitle(), firstElement.title());
        assert Objects.equals(contents6.getName(), firstElement.contentsName1());
        assert Objects.equals(contents5.getName(), firstElement.contentsName2());
        assert Objects.equals(mediaFile6.getFilePath(), firstElement.filePath1());
        assert Objects.equals(mediaFile5.getFilePath(), firstElement.filePath2());

        assert secondElement.id() == 1;
        assert Objects.equals(game1.getTitle(), secondElement.title());
        assert Objects.equals(game1.getDescription(), secondElement.description());
        assert Objects.equals(contents4.getName(), secondElement.contentsName1());
        assert Objects.equals(contents3.getName(), secondElement.contentsName2());
        assert Objects.equals(mediaFile4.getFilePath(), secondElement.filePath1());
        assert Objects.equals(mediaFile3.getFilePath(), secondElement.filePath2());

    }

    @Test
    @DisplayName("모든 월드컵 게임, 페이징 조회 성공 - 게임 0개")
    public void 모든_월드컵_게임_페이징_조회_성공_컨텐츠_없음() {
        String worldCupGameKeyword = "test";
        LocalDate startDate = LocalDate.now().minusDays(2);
        LocalDate endDate = LocalDate.now();
        Pageable pageable = PageRequest.of(0, 25, Sort.Direction.DESC, "id");

        Page<GetWorldCupGamePageProjection> result = worldCupGameRepository.getWorldCupGamePage(
                startDate,
                endDate,
                worldCupGameKeyword,
                pageable
        );
        result.getContent().forEach(it ->
                System.out.println(it.id() + ", " + it.contentsName1() + ", " + it.contentsName2())
        );
        assert result.getTotalPages() == 0;
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


    @ParameterizedTest
    @CsvSource(value = {
            "한국드라마",
            "한국  드라마",
            "   한국  드라마   ",
            "한 국 드 라 마",
    })
    @DisplayName("모든 월드컵 게임, 페이징 조회 성공 - 키워드 조건 적용")
    public void 모든_월드컵_게임_페이징_조회_성공_키워드(String worldCupGameKeyword) {
        WorldCupGame game1 = createWorldCupGame("한국 드라마 월드컵(2000~23.10.04)", "2000년부터 현재까지 한국드라마...", WorldCupGameRound.ROUND_16, VisibleType.PRIVATE, 1);
        WorldCupGame game2 = createWorldCupGame("2022 좋은 노트북 월드컵", "2022년 월드컵 []", WorldCupGameRound.ROUND_4, VisibleType.PRIVATE, 1);

        MediaFile mediaFile1 = createMediaFile("originalName1", "A345ytgs32eff1", "https://s3.dsfwwg4fsesef1/aawr.com", ".png");
        MediaFile mediaFile2 = createMediaFile("originalName2", "A345ytgs32eff2", "https://s3.dsfwwg4fsesef2/aawr.com", ".png");
        MediaFile mediaFile3 = createMediaFile("originalName3", "A345ytgs32eff3", "https://s3.dsfwwg4fsesef3/aawr.com", ".png");
        MediaFile mediaFile4 = createMediaFile("originalName4", "A345ytgs32eff4", "https://s3.dsfwwg4fsesef4/aawr.com", ".png");
        MediaFile mediaFile5 = createMediaFile("originalName5", "A345ytgs32eff5", "https://s3.dsfwwg4fsesef5/aawr.com", ".png");
        MediaFile mediaFile6 = createMediaFile("originalName6", "A345ytgs32eff6", "https://s3.dsfwwg4fsesef6/aawr.com", ".jpg");

        WorldCupGameContents contents1 = createGameContents(game1, "태양의 후예", 1);
        WorldCupGameContents contents2 = createGameContents(game1, "태조왕건", 2);
        WorldCupGameContents contents3 = createGameContents(game1, "도깨비", 3);
        WorldCupGameContents contents4 = createGameContents(game1, "마스크 걸", 4);
        WorldCupGameContents contents5 = createGameContents(game2, "맥북 13인치", 5);
        WorldCupGameContents contents6 = createGameContents(game2, "갤럭시북 2021", 6);

        worldCupGameRepository.saveAll(List.of(game1, game2));
        mediaFileRepository.saveAll(List.of(mediaFile1, mediaFile2, mediaFile3, mediaFile4, mediaFile5, mediaFile6));
        worldCupGameContentsRepository.saveAll(List.of(contents1, contents2, contents3, contents4, contents5, contents6));

        LocalDate startDate = LocalDate.now().minusDays(2);
        LocalDate endDate = LocalDate.now();
        Pageable pageable = PageRequest.of(0, 25, Sort.Direction.DESC, "id");

        Page<GetWorldCupGamePageProjection> result = worldCupGameRepository.getWorldCupGamePage(
                startDate,
                endDate,
                worldCupGameKeyword,
                pageable
        );
        System.out.println("조회 결과");
        result.getContent().forEach(it ->
                System.out.println(it.id() + ", " + it.contentsName1() + ", " + it.contentsName2())
        );

        GetWorldCupGamePageProjection firstElement = result.getContent().get(0);

        assert result.getTotalPages() == 1;
        assert result.getContent().size() == 1;

        assert firstElement.id() == 1;
        assert Objects.equals(game1.getTitle(), firstElement.title());
        assert Objects.equals(game1.getDescription(), firstElement.description());
        assert Objects.equals(contents4.getName(), firstElement.contentsName1());
        assert Objects.equals(contents3.getName(), firstElement.contentsName2());
        assert Objects.equals(mediaFile4.getFilePath(), firstElement.filePath1());
        assert Objects.equals(mediaFile3.getFilePath(), firstElement.filePath2());

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
        WorldCupGame game1 = createWorldCupGame("한국 드라마 월드컵(2000~23.10.04)", "2000년부터 현재까지 한국드라마...", WorldCupGameRound.ROUND_16, VisibleType.PRIVATE, 1);
        WorldCupGame game2 = createWorldCupGame("2022 좋은 노트북 월드컵", "2022년 월드컵 []", WorldCupGameRound.ROUND_4, VisibleType.PRIVATE, 1);

        MediaFile mediaFile1 = createMediaFile("originalName1", "A345ytgs32eff1", "https://s3.dsfwwg4fsesef1/aawr.com", ".png");
        MediaFile mediaFile2 = createMediaFile("originalName2", "A345ytgs32eff2", "https://s3.dsfwwg4fsesef2/aawr.com", ".png");
        MediaFile mediaFile3 = createMediaFile("originalName3", "A345ytgs32eff3", "https://s3.dsfwwg4fsesef3/aawr.com", ".png");
        MediaFile mediaFile4 = createMediaFile("originalName4", "A345ytgs32eff4", "https://s3.dsfwwg4fsesef4/aawr.com", ".png");
        MediaFile mediaFile5 = createMediaFile("originalName5", "A345ytgs32eff5", "https://s3.dsfwwg4fsesef5/aawr.com", ".png");
        MediaFile mediaFile6 = createMediaFile("originalName6", "A345ytgs32eff6", "https://s3.dsfwwg4fsesef6/aawr.com", ".jpg");

        WorldCupGameContents contents1 = createGameContents(game1, "태양의 후예", 1);
        WorldCupGameContents contents2 = createGameContents(game1, "태조왕건", 2);
        WorldCupGameContents contents3 = createGameContents(game1, "도깨비", 3);
        WorldCupGameContents contents4 = createGameContents(game1, "마스크 걸", 4);
        WorldCupGameContents contents5 = createGameContents(game2, "맥북 13인치", 5);
        WorldCupGameContents contents6 = createGameContents(game2, "갤럭시북 2021", 6);

        worldCupGameRepository.saveAll(List.of(game1, game2));
        mediaFileRepository.saveAll(List.of(mediaFile1, mediaFile2, mediaFile3, mediaFile4, mediaFile5, mediaFile6));
        worldCupGameContentsRepository.saveAll(List.of(contents1, contents2, contents3, contents4, contents5, contents6));

        LocalDate startDate = LocalDate.now().minusDays(2);
        LocalDate endDate = LocalDate.now();
        Pageable pageable = PageRequest.of(0, 25, Sort.Direction.DESC, "id");

        Page<GetWorldCupGamePageProjection> result = worldCupGameRepository.getWorldCupGamePage(
                startDate,
                endDate,
                worldCupGameKeyword,
                pageable
        );
        System.out.println("조회 결과");
        result.getContent().forEach(it ->
                System.out.println(it.id() + ", " + it.contentsName1() + ", " + it.contentsName2())
        );

        assert result.getTotalPages() == 1;
        assert result.getContent().size() == 0;
        assert result.getNumberOfElements() == 0;
        assert result.getNumber() == 0;

    }
}
