package com.example.demo.worldcup.service;

import com.example.demo.domain.etc.model.MediaFile;
import com.example.demo.domain.etc.repository.MediaFileRepository;
import com.example.demo.domain.worldcup.controller.response.GetAvailableGameRoundsResponse;
import com.example.demo.domain.worldcup.controller.response.GetWorldCupPlayContentsResponse;
import com.example.demo.domain.worldcup.exception.IllegalWorldCupGameContentsException;
import com.example.demo.domain.worldcup.exception.NoRoundsAvailableToPlayException;
import com.example.demo.domain.worldcup.exception.NotFoundWorldCupGameException;
import com.example.demo.domain.worldcup.model.WorldCupGame;
import com.example.demo.domain.worldcup.model.WorldCupGameContents;
import com.example.demo.domain.worldcup.model.vo.VisibleType;
import com.example.demo.domain.worldcup.model.vo.WorldCupGameRound;
import com.example.demo.domain.worldcup.repository.WorldCupGameContentsRepository;
import com.example.demo.domain.worldcup.repository.WorldCupGameRepository;
import com.example.demo.domain.worldcup.repository.projection.GetAvailableGameRoundsProjection;
import com.example.demo.domain.worldcup.repository.projection.GetDividedWorldCupGameContentsProjection;
import com.example.demo.domain.worldcup.service.WorldCupGameContentsService;
import com.example.demo.helper.DataBaseCleanUp;
import com.google.common.collect.Lists;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.zip.GZIPInputStream;

import static com.example.demo.domain.worldcup.model.vo.VisibleType.*;
import static com.example.demo.domain.worldcup.model.vo.WorldCupGameRound.ROUND_32;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest
@ActiveProfiles("test")
public class WorldCupContentsServiceTest {

    @Autowired
    private WorldCupGameContentsService worldCupGamecontentsService;
    @SpyBean
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


    @Test
    @DisplayName("월드컵 게임 라운드 수 조회")
    public void getAvailableGameRounds1() {

        // given
        WorldCupGame worldCupGame = WorldCupGame
                .builder()
                .title("title1")
                .description("description1")
                .round(ROUND_32)
                .visibleType(PUBLIC)
                .views(0)
                .softDelete(false)
                .memberId(1)
                .build();


        List<WorldCupGameContents> contentsList = range(1, 10)
                .mapToObj(idx ->
                    WorldCupGameContents.builder()
                            .name("contentsName")
                            .worldCupGame(worldCupGame)
                            .mediaFileId(1)
                            .build()
                )
                .toList();
        worldCupGameRepository.save(worldCupGame);
        worldCupGameContentsRepository.saveAll(contentsList);

        // when
        GetAvailableGameRoundsResponse result = worldCupGamecontentsService.getAvailableGameRounds(1L);

        // then
        assert result.worldCupId() == 1;
        assert Objects.equals(result.worldCupDescription(), "description1");
        assert result.rounds().equals(List.of(2, 4, 8));
    }

    @Test
    @DisplayName("월드컵 게임 라운드 수 조회 - 게임에 플레이 가능한 라운드가 존재하지 않음 (예외)")
    public void getAvailableGameRounds2() {

        // given
        WorldCupGame worldCupGame = WorldCupGame
                .builder()
                .title("title1")
                .description("description1")
                .round(ROUND_32)
                .visibleType(PUBLIC)
                .views(0)
                .softDelete(false)
                .memberId(1)
                .build();
        worldCupGameRepository.save(worldCupGame);

        // when & then
        assertThrows(
                NoRoundsAvailableToPlayException.class,
                () -> worldCupGamecontentsService.getAvailableGameRounds(1L)
        );
    }

    @Test
    @DisplayName("월드컵 게임 라운드 수 조회 - 존재하지 않는 게임을 조회할 수 없음 (예외)")
    public void getAvailableGameRounds3() {

        // Not Given

        // when
        assertThrows(
                NotFoundWorldCupGameException.class,
                () -> worldCupGamecontentsService.getAvailableGameRounds(1L)
        );
    }


    @Test
    @DisplayName("이상형 월드컵 게임 플레이를 위한 컨텐츠 조회 - 예상한 컨텐츠 조회 사이즈와 실제 조회 사이즈가 다르면 안된다. (예외)")
    public void GetPlayContents1() {

        // given
        WorldCupGame worldCupGame = WorldCupGame
                .builder()
                .title("title1")
                .description("description1")
                .round(ROUND_32)
                .visibleType(PUBLIC)
                .views(0)
                .softDelete(false)
                .memberId(1)
                .build();

        List<MediaFile> mediaFiles = range(1, 10).mapToObj(idx ->
                MediaFile.builder()
                        .originalName("fileOriginalName")
                        .absoluteName("fileAbsoluteName")
                        .filePath("/naver/.../")
                        .extension(".png")
                        .build()
        ).toList();
        List<WorldCupGameContents> contentsList = range(1, 10).mapToObj(idx ->
                WorldCupGameContents.builder()
                        .name("contentsName")
                        .worldCupGame(worldCupGame)
                        .mediaFileId(idx)
                        .build()
        ).toList();

        worldCupGameRepository.save(worldCupGame);
        mediaFileRepository.saveAll(mediaFiles);
        worldCupGameContentsRepository.saveAll(contentsList);

        List<GetDividedWorldCupGameContentsProjection> ACTUAL_GET_CONTENTS_LIST = List.of(
                new GetDividedWorldCupGameContentsProjection(1, "name", "absoulteName", "filePath"),
                new GetDividedWorldCupGameContentsProjection(2, "name", "absoulteName", "filePath")
        );
        given(worldCupGameRepository.getDividedWorldCupGameContents(1L, 8, List.of()))
                .willReturn(ACTUAL_GET_CONTENTS_LIST);


        // when & then
        IllegalWorldCupGameContentsException resultException =
                assertThrows(
                        IllegalWorldCupGameContentsException.class,
                        () -> worldCupGamecontentsService.getPlayContents(
                                1L,
                                8,
                                1,
                                List.of()
                )
        );

        assert resultException.getPublicMessage().contains("조회 컨텐츠 수가 다름 ");
    }

    @Test
    @DisplayName("이상형 월드컵 게임 플레이를 위한 컨텐츠 조회 - 이미 플레이한 게임 컨텐츠를 조회하면 안된다. (예외)")
    public void GetPlayContents2() {

        // given
        WorldCupGame worldCupGame = WorldCupGame
                .builder()
                .title("title1")
                .description("description1")
                .round(ROUND_32)
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
                .mediaFileId(1)
                .build();
        WorldCupGameContents contents2 = WorldCupGameContents.builder()
                .name("contentsName")
                .worldCupGame(worldCupGame)
                .mediaFileId(2)
                .build();

        List<GetDividedWorldCupGameContentsProjection> ACTUAL_GET_CONTENTS_LIST = List.of(
                new GetDividedWorldCupGameContentsProjection(1, "name", "absoulteName", "filePath"),
                new GetDividedWorldCupGameContentsProjection(2, "name", "absoulteName", "filePath")
        );
        given(worldCupGameRepository.getDividedWorldCupGameContents(1L, 2, List.of(1L)))
                .willReturn(ACTUAL_GET_CONTENTS_LIST);

        worldCupGameRepository.save(worldCupGame);
        mediaFileRepository.saveAll(List.of(mediaFile1, mediaFile2));
        worldCupGameContentsRepository.saveAll(List.of(contents1, contents2));


        // when & then
        final List<Long> ALREADY_PLAYED_CONTENTS_ID = List.of(1L);
        IllegalWorldCupGameContentsException resultException =
                assertThrows(
                        IllegalWorldCupGameContentsException.class,
                        () -> worldCupGamecontentsService.getPlayContents(
                                1L,
                                2,
                                1,
                                ALREADY_PLAYED_CONTENTS_ID
                        )
                );
        
        assert resultException.getPublicMessage().contains("컨텐츠 중복");
    }
}
