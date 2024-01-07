package com.masikga.itwc.domain.worldcup.repository;

import static com.masikga.itwc.domain.worldcup.model.vo.VisibleType.*;
import static com.masikga.itwc.domain.worldcup.model.vo.WorldCupGameRound.*;
import static com.masikga.itwc.helper.TestConstant.*;
import static java.util.stream.Collectors.*;
import static java.util.stream.IntStream.range;
import static java.util.stream.LongStream.range;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.masikga.itwc.domain.etc.model.InternetVideoUrl;
import com.masikga.itwc.domain.etc.model.MediaFile;
import com.masikga.itwc.domain.etc.model.StaticMediaFile;
import com.masikga.itwc.domain.etc.repository.AbstractMediaFileRepository;
import com.masikga.itwc.domain.worldcup.model.WorldCupGame;
import com.masikga.itwc.domain.worldcup.model.WorldCupGameContents;
import com.masikga.itwc.domain.worldcup.model.vo.VisibleType;
import com.masikga.itwc.domain.worldcup.model.vo.WorldCupGameRound;
import com.masikga.itwc.domain.worldcup.repository.projection.GetDividedWorldCupGameContentsProjection;
import com.masikga.itwc.helper.DataBaseCleanUp;
import com.masikga.itwc.helper.testbase.IntegrationBaseTest;

public class WorldCupGameRepositoryTest implements IntegrationBaseTest {

	@Autowired
	private WorldCupGameRepository worldCupGameRepository;

	@Autowired
	private WorldCupGameContentsRepository worldCupGameContentsRepository;

	@Autowired
	private AbstractMediaFileRepository abstractMediaFileRepository;

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
		@DisplayName(SUCCESS_PREFIX + "8개 컨텐츠 모두 조회 (정적 파일 4개, 인터넷 동영상 URL 4개)")
		public void success1() {
			// given
			WorldCupGame worldCupGame = createWorldCupGame("TITLE1", "DESC1", ROUND_32, PUBLIC, 1);

			List<StaticMediaFile> mediaFiles = range(1, 5)
				.mapToObj(idx ->
					createMediaFile(
						"ORIGINAL" + idx,
						"ABSOLUTE" + idx,
						"s3://abc/",
						".png"))
				.toList();
			List<InternetVideoUrl> internetMovieUrls = range(5, 9)
				.mapToObj(idx ->
					InternetVideoUrl.builder()
						.objectKey("www.youtube/dogmovies/" + idx)
						.isPlayableVideo(true)
						.videoStartTime("00000")
						.videoPlayDuration(3)
						.build()
				).toList();
			List<WorldCupGameContents> contentsListWithMediaFile = range(1, 5)
				.mapToObj(idx ->
					createGameContents(
						worldCupGame,
						"NAME" + idx,
						mediaFiles.get(idx - 1)
					))
				.toList();
			List<WorldCupGameContents> contentsListWithInternetMovie = range(5, 9)
				.mapToObj(idx ->
					createGameContents(
						worldCupGame,
						"NAME" + idx,
						internetMovieUrls.get(idx - 5)
					))
				.toList();

			worldCupGameRepository.save(worldCupGame);
			abstractMediaFileRepository.saveAll(mediaFiles);
			abstractMediaFileRepository.saveAll(internetMovieUrls);
			worldCupGameContentsRepository.saveAll(contentsListWithMediaFile);
			worldCupGameContentsRepository.saveAll(contentsListWithInternetMovie);

			// when
			List<GetDividedWorldCupGameContentsProjection> result = worldCupGameRepository.getDividedWorldCupGameContents(
				1L,
				8,
				List.of()
			);

			assertAll(
				() -> assertThat(result.size()).isEqualTo(8),

				() -> assertThat(result.get(0).mediaFileId()).isEqualTo(1),
				() -> assertThat(result.get(0).movieStartTime()).isEqualTo(null),
				() -> assertThat(result.get(0).moviePlayDuration()).isEqualTo(null),
				() -> assertThat(result.get(1).mediaFileId()).isEqualTo(2),
				() -> assertThat(result.get(1).movieStartTime()).isEqualTo(null),
				() -> assertThat(result.get(1).moviePlayDuration()).isEqualTo(null),
				() -> assertThat(result.get(2).mediaFileId()).isEqualTo(3),
				() -> assertThat(result.get(2).movieStartTime()).isEqualTo(null),
				() -> assertThat(result.get(2).moviePlayDuration()).isEqualTo(null),
				() -> assertThat(result.get(3).mediaFileId()).isEqualTo(4),
				() -> assertThat(result.get(3).movieStartTime()).isEqualTo(null),
				() -> assertThat(result.get(3).moviePlayDuration()).isEqualTo(null),

				() -> assertThat(result.get(4).mediaFileId()).isEqualTo(5),
				() -> assertThat(result.get(4).movieStartTime()).isEqualTo("00000"),
				() -> assertThat(result.get(4).moviePlayDuration()).isEqualTo(3),
				() -> assertThat(result.get(5).mediaFileId()).isEqualTo(6),
				() -> assertThat(result.get(5).movieStartTime()).isEqualTo("00000"),
				() -> assertThat(result.get(5).moviePlayDuration()).isEqualTo(3),
				() -> assertThat(result.get(6).mediaFileId()).isEqualTo(7),
				() -> assertThat(result.get(6).movieStartTime()).isEqualTo("00000"),
				() -> assertThat(result.get(6).moviePlayDuration()).isEqualTo(3),
				() -> assertThat(result.get(7).mediaFileId()).isEqualTo(8),
				() -> assertThat(result.get(7).movieStartTime()).isEqualTo("00000"),
				() -> assertThat(result.get(7).moviePlayDuration()).isEqualTo(3)
			);
		}

		@Test
		@DisplayName(SUCCESS_PREFIX + "12개 컨텐츠 중 6개 조회 (정적 파일 3개, 인터넷 동영상 URL 9개)")
		public void success2() {
			// given
			WorldCupGame worldCupGame = createWorldCupGame(
				"TITLE1",
				"DESC1",
				ROUND_32,
				PUBLIC,
				1
			);

			List<StaticMediaFile> mediaFiles = range(1, 4)
				.mapToObj(idx ->
					createMediaFile(
						"ORIGINAL" + idx,
						"ABSOLUTE" + idx,
						"s3://abc/",
						".png"))
				.toList();
			List<InternetVideoUrl> internetMovieUrls = range(5, 14)
				.mapToObj(idx ->
					InternetVideoUrl.builder()
						.isPlayableVideo(true)
						.videoPlayDuration(3)
						.videoStartTime("00001")
						.objectKey("path/" + idx)
						.build())
				.toList();
			List<WorldCupGameContents> contentsListWithMediaFiles = range(1, 4)
				.mapToObj(idx ->
					createGameContents(
						worldCupGame,
						"NAME" + idx,
						mediaFiles.get(idx - 1)
					))
				.toList();
			List<WorldCupGameContents> contentsListWithMovieUrls = range(5, 14)
				.mapToObj(idx ->
					createGameContents(
						worldCupGame,
						"NAME" + idx,
						internetMovieUrls.get(idx - 5)
					))
				.toList();

			worldCupGameRepository.save(worldCupGame);
			abstractMediaFileRepository.saveAll(mediaFiles);
			abstractMediaFileRepository.saveAll(internetMovieUrls);
			worldCupGameContentsRepository.saveAll(contentsListWithMediaFiles);
			worldCupGameContentsRepository.saveAll(contentsListWithMovieUrls);

			// when
			List<GetDividedWorldCupGameContentsProjection> result = worldCupGameRepository.getDividedWorldCupGameContents(
				1L,
				6,
				List.of()
			);

			assertAll(
				() -> assertThat(result.get(0).FileType()).isEqualTo("STATIC_MEDIA_FILE"),
				() -> assertThat(result.get(0).moviePlayDuration()).isEqualTo(null),
				() -> assertThat(result.get(0).movieStartTime()).isEqualTo(null),
				() -> assertThat(result.get(0).mediaFileId()).isEqualTo(1),

				() -> assertThat(result.get(1).FileType()).isEqualTo("STATIC_MEDIA_FILE"),
				() -> assertThat(result.get(1).moviePlayDuration()).isEqualTo(null),
				() -> assertThat(result.get(1).movieStartTime()).isEqualTo(null),
				() -> assertThat(result.get(1).mediaFileId()).isEqualTo(2),

				() -> assertThat(result.get(2).FileType()).isEqualTo("STATIC_MEDIA_FILE"),
				() -> assertThat(result.get(2).moviePlayDuration()).isEqualTo(null),
				() -> assertThat(result.get(2).movieStartTime()).isEqualTo(null),
				() -> assertThat(result.get(2).mediaFileId()).isEqualTo(3),

				() -> assertThat(result.get(3).FileType()).isEqualTo("INTERNET_VIDEO_URL"),
				() -> assertThat(result.get(3).moviePlayDuration()).isEqualTo(3),
				() -> assertThat(result.get(3).movieStartTime()).isEqualTo("00001"),
				() -> assertThat(result.get(3).mediaFileId()).isEqualTo(4),

				() -> assertThat(result.get(4).FileType()).isEqualTo("INTERNET_VIDEO_URL"),
				() -> assertThat(result.get(4).moviePlayDuration()).isEqualTo(3),
				() -> assertThat(result.get(4).movieStartTime()).isEqualTo("00001"),
				() -> assertThat(result.get(4).mediaFileId()).isEqualTo(5),

				() -> assertThat(result.get(5).FileType()).isEqualTo("INTERNET_VIDEO_URL"),
				() -> assertThat(result.get(5).moviePlayDuration()).isEqualTo(3),
				() -> assertThat(result.get(5).movieStartTime()).isEqualTo("00001"),
				() -> assertThat(result.get(5).mediaFileId()).isEqualTo(6)
			);
		}

		@Test
		@DisplayName(SUCCESS_PREFIX + "12개 컨텐츠 중 6개 조회 (이미 플레이한 이상형 목록 6개를 제외) (정적 파일 12개)")
		public void success3() {
			// given
			WorldCupGame worldCupGame = createWorldCupGame(
				"TITLE1",
				"DESC1",
				ROUND_32,
				PUBLIC,
				1
			);
			List<StaticMediaFile> mediaFiles = range(1, 13)
				.mapToObj(idx ->
					createMediaFile(
						"ORIGINAL" + idx,
						"ABSOLUTE" + idx,
						"s3://abc/",
						".png"))
				.collect(toList());
			List<WorldCupGameContents> contentsList = range(1, 13)
				.mapToObj(idx ->
					createGameContents(
						worldCupGame,
						"NAME" + idx,
						mediaFiles.get(idx - 1)
					))
				.collect(toList());

			worldCupGameRepository.save(worldCupGame);
			abstractMediaFileRepository.saveAll(mediaFiles);
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
			assertThat(result.size()).isEqualTo(6);
		}

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
			.build();
	}

	private WorldCupGameContents createGameContents(WorldCupGame game, String name, MediaFile abstractMediaFile) {
		return WorldCupGameContents.builder()
			.name(name)
			.worldCupGame(game)
			.mediaFile(abstractMediaFile)
			.build();
	}
}
