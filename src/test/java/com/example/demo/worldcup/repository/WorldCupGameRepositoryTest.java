package com.example.demo.worldcup.repository;

import com.example.demo.domain.etc.model.MediaFile;
import com.example.demo.domain.etc.model.MediaFileRepository;
import com.example.demo.helper.DataBaseCleanUp;
import com.example.demo.domain.worldcup.model.repository.WorldCupGameContentsRepository;
import com.example.demo.domain.worldcup.model.entity.WorldCupGame;
import com.example.demo.domain.worldcup.model.repository.WorldCupGameRepository;
import com.example.demo.domain.worldcup.model.entity.WorldCupGameContents;
import com.example.demo.domain.worldcup.model.entity.vo.WorldCupGameRound;
import com.example.demo.domain.worldcup.model.entity.vo.VisibleType;
import com.example.demo.domain.worldcup.model.projection.FindWorldCupGamePageProjection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

        String keyword = "test";
        LocalDate startDate = LocalDate.now().minusDays(2);
        LocalDate endDate = LocalDate.now();
        Pageable pageable = Pageable.ofSize(25);

        Page<FindWorldCupGamePageProjection> result = worldCupGameRepository.findWorldCupGamePage(
                startDate,
                endDate,
                pageable
        );

        result.getContent().forEach(it ->
                System.out.println(it.getId() + ", " + it.getContentsName1() + ", " + it.getContentsName2())
        );

        FindWorldCupGamePageProjection firstElement = result.getContent().get(0);
        FindWorldCupGamePageProjection secondElement = result.getContent().get(1);

        assert result.getTotalPages() == 1;
        assert result.getContent().size() == 2;
        assert result.getNumberOfElements() == 2;
        assert result.getNumber() == 0;

        assert firstElement.getId() == 2;
        assert Objects.equals(game2.getTitle(), firstElement.getTitle());
        assert Objects.equals(contents6.getName(), firstElement.getContentsName1());
        assert Objects.equals(contents5.getName(), firstElement.getContentsName2());
        assert Objects.equals(mediaFile6.getFilePath(), firstElement.getFilePath1());
        assert Objects.equals(mediaFile5.getFilePath(), firstElement.getFilePath2());

        assert secondElement.getId() == 1;
        assert Objects.equals(game1.getTitle(), secondElement.getTitle());
        assert Objects.equals(game1.getDescription(), secondElement.getDescription());
        assert Objects.equals(contents4.getName(), secondElement.getContentsName1());
        assert Objects.equals(contents3.getName(), secondElement.getContentsName2());
        assert Objects.equals(mediaFile4.getFilePath(), secondElement.getFilePath1());
        assert Objects.equals(mediaFile3.getFilePath(), secondElement.getFilePath2());

    }

    @Test
    @DisplayName("모든 월드컵 게임, 페이징 조회 성공 - 게임 0개")
    public void 모든_월드컵_게임_페이징_조회_성공_컨텐츠_없음() {
        String keyword = "test";
        LocalDate startDate = LocalDate.now().minusDays(2);
        LocalDate endDate = LocalDate.now();
        Pageable pageable = Pageable.ofSize(25);

        Page<FindWorldCupGamePageProjection> result = worldCupGameRepository.findWorldCupGamePage(
                startDate,
                endDate,
                pageable
        );
        result.getContent().forEach(it ->
                System.out.println(it.getId() + ", " + it.getContentsName1() + ", " + it.getContentsName2())
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
}
