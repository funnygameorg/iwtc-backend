package com.example.demo.worldcup.repository;

import com.example.demo.domain.etc.model.MediaFile;
import com.example.demo.domain.etc.repository.MediaFileRepository;
import com.example.demo.domain.worldcup.model.WorldCupGame;
import com.example.demo.domain.worldcup.model.WorldCupGameContents;
import com.example.demo.domain.worldcup.model.vo.VisibleType;
import com.example.demo.domain.worldcup.model.vo.WorldCupGameRound;
import com.example.demo.domain.worldcup.repository.WorldCupGameContentsRepository;
import com.example.demo.domain.worldcup.repository.WorldCupGameRepository;
import com.example.demo.domain.worldcup.repository.projection.GetDividedWorldCupGameContentsProjection;
import com.example.demo.helper.DataBaseCleanUp;
import com.example.demo.helper.testbase.IntegrationBaseTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

import static com.example.demo.helper.TestConstant.SUCCESS_PREFIX;
import static com.example.demo.domain.worldcup.model.vo.VisibleType.*;
import static com.example.demo.domain.worldcup.model.vo.WorldCupGameRound.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static java.util.stream.LongStream.range;

public class WorldCupGameRepositoryTest implements IntegrationBaseTest {

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
    @DisplayName("월드컵 게임 플레이 컨텐츠 리스트 조회할 수 있다.")
    public class getDividedWorldCupGameContents {
        @Test
        @DisplayName(SUCCESS_PREFIX + "3개 컨텐츠 중 3개 조회")
        public void success1() {
            // given
            WorldCupGame worldCupGame = createWorldCupGame("TITLE1", "DESC1", ROUND_32, PUBLIC, 1);

            List<MediaFile> mediaFiles = range(1, 4)
                    .mapToObj( idx ->
                            createMediaFile(
                                    "ORIGINAL"+idx,
                                    "ABSOLUTE"+idx,
                                    "s3://abc/",
                                    ".png"))
                    .collect(toList());

            List<WorldCupGameContents> contentsList = range(1, 4)
                    .mapToObj( idx ->
                            createGameContents(
                                    worldCupGame,
                                    "NAME"+idx,
                                    mediaFiles.get(idx - 1)
                            ))
                    .collect(toList());

            worldCupGameRepository.save(worldCupGame);
            mediaFileRepository.saveAll(mediaFiles);
            worldCupGameContentsRepository.saveAll(contentsList);

            Long worldCupId = 1L;
            int divideContentsSizePerRequest = 3;
            List<Long> alreadyPlayedContentsIds = List.of();

            // when
            List<GetDividedWorldCupGameContentsProjection> result = worldCupGameRepository.getDividedWorldCupGameContents(
                    worldCupId,
                    divideContentsSizePerRequest,
                    alreadyPlayedContentsIds
            );

            assert result.size() == 3;

            assert Objects.equals(result.get(0).name(), contentsList.get(0).getName());
            assert Objects.equals(result.get(1).name(), contentsList.get(1).getName());
            assert Objects.equals(result.get(2).name(), contentsList.get(2).getName());

            assert Objects.equals(result.get(0).absoluteName(), mediaFiles.get(0).getAbsoluteName());
            assert Objects.equals(result.get(1).absoluteName(), mediaFiles.get(1).getAbsoluteName());
            assert Objects.equals(result.get(2).absoluteName(), mediaFiles.get(2).getAbsoluteName());
        }

        @Test
        @DisplayName(SUCCESS_PREFIX + "12개 컨텐츠 중 6개 조회")
        public void success2() {
            // given
            WorldCupGame worldCupGame = createWorldCupGame(
                    "TITLE1",
                    "DESC1",
                    ROUND_32,
                    PUBLIC,
                    1
            );

            List<MediaFile> mediaFiles = range(1, 13)
                    .mapToObj( idx ->
                            createMediaFile(
                                    "ORIGINAL"+idx,
                                    "ABSOLUTE"+idx,
                                    "s3://abc/",
                                    ".png"))
                    .collect(toList());

            List<WorldCupGameContents> contentsList = range(1, 13)
                    .mapToObj( idx ->
                            createGameContents(
                                    worldCupGame,
                                    "NAME"+idx,
                                    mediaFiles.get(idx - 1)
                            ))
                    .collect(toList());

            worldCupGameRepository.save(worldCupGame);
            mediaFileRepository.saveAll(mediaFiles);
            worldCupGameContentsRepository.saveAll(contentsList);

            Long worldCupId = 1L;
            int divideContentsSizePerRequest = contentsList.size() / 2;
            List<Long> alreadyPlayedContentsIds = List.of();

            // when
            List<GetDividedWorldCupGameContentsProjection> result = worldCupGameRepository.getDividedWorldCupGameContents(
                    worldCupId,
                    divideContentsSizePerRequest,
                    alreadyPlayedContentsIds
            );

            assert result.size() == 6;
        }

        @Test
        @DisplayName(SUCCESS_PREFIX + "12개 컨텐츠 중 6개 조회 (이미 플레이한 이상형 목록 6개 추가)")
        public void success3() {
            // given
            WorldCupGame worldCupGame = createWorldCupGame(
                    "TITLE1",
                    "DESC1",
                    ROUND_32,
                    PUBLIC,
                    1
            );
            List<MediaFile> mediaFiles = range(1, 13)
                    .mapToObj( idx ->
                            createMediaFile(
                                    "ORIGINAL"+idx,
                                    "ABSOLUTE"+idx,
                                    "s3://abc/",
                                    ".png"))
                    .collect(toList());
            List<WorldCupGameContents> contentsList = range(1, 13)
                    .mapToObj( idx ->
                            createGameContents(
                                    worldCupGame,
                                    "NAME"+idx,
                                    mediaFiles.get(idx - 1)
                            ))
                    .collect(toList());

            worldCupGameRepository.save(worldCupGame);
            mediaFileRepository.saveAll(mediaFiles);
            worldCupGameContentsRepository.saveAll(contentsList);

            Long worldCupId = 1L;
            int divideContentsSizePerRequest = contentsList.size() / 2;
            List<Long> alreadyPlayedContentsIds = range(1L, 7L).boxed().toList();

            // when
            List<GetDividedWorldCupGameContentsProjection> result = worldCupGameRepository.getDividedWorldCupGameContents(
                    worldCupId,
                    divideContentsSizePerRequest,
                    alreadyPlayedContentsIds
            );

            assert result.size() == 6;
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

    private WorldCupGameContents createGameContents(WorldCupGame game, String name, MediaFile mediaFile) {
        return WorldCupGameContents.builder()
                .name(name)
                .worldCupGame(game)
                .mediaFile(mediaFile)
                .build();
    }
}
