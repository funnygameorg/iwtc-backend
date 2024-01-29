package com.masikga.itwc.domain.worldcup.service;

import static com.masikga.itwc.domain.etc.model.vo.FileType.*;
import static com.masikga.itwc.domain.worldcup.controller.request.CreateWorldCupContentsRequest.*;
import static com.masikga.itwc.domain.worldcup.model.vo.VisibleType.*;
import static com.masikga.itwc.helper.TestConstant.*;
import static java.util.List.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.masikga.itwc.domain.etc.model.InternetVideoUrl;
import com.masikga.itwc.domain.etc.model.StaticMediaFile;
import com.masikga.itwc.domain.etc.repository.MediaFileRepository;
import com.masikga.itwc.domain.worldcup.controller.request.CreateWorldCupRequest;
import com.masikga.itwc.domain.worldcup.controller.response.GetMyWorldCupResponse;
import com.masikga.itwc.domain.worldcup.controller.response.GetWorldCupContentsResponse;
import com.masikga.itwc.domain.worldcup.exception.DuplicatedWorldCupGameTitleException;
import com.masikga.itwc.domain.worldcup.exception.NotFoundWorldCupGameException;
import com.masikga.itwc.domain.worldcup.exception.NotOwnerGameException;
import com.masikga.itwc.domain.worldcup.model.WorldCupGame;
import com.masikga.itwc.domain.worldcup.model.WorldCupGameContents;
import com.masikga.itwc.domain.worldcup.repository.WorldCupGameContentsRepository;
import com.masikga.itwc.domain.worldcup.repository.WorldCupGameRepository;
import com.masikga.itwc.helper.DataBaseCleanUp;
import com.masikga.itwc.helper.testbase.IntegrationBaseTest;
import com.masikga.itwc.infra.filestorage.FileStorage;

public class WorldCupBasedOnServiceTest implements IntegrationBaseTest {

	@Autowired
	private WorldCupBasedOnAuthService worldCupBasedOnAuthService;
	@Autowired
	private WorldCupGameRepository worldCupGameRepository;
	@Autowired
	private MediaFileRepository mediaFileRepository;
	@Autowired
	private WorldCupGameContentsRepository worldCupGameContentsRepository;
	@MockBean
	private FileStorage s3Component;
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
				.visibleType(PUBLIC)
				.memberId(1)
				.build();
			StaticMediaFile mediaFile1 = StaticMediaFile.builder()
				.originalName("fileOriginalName")
				.objectKey("https://www.abc.com/BS/1")
				.extension("PNG")
				.originalFileSize("0")
				.build();
			StaticMediaFile mediaFile2 = StaticMediaFile.builder()
				.originalName("fileOriginalName")
				.objectKey("https://www.abc.com/BS/2")
				.extension("PNG")
				.originalFileSize("0")
				.build();
			WorldCupGameContents contents1 = WorldCupGameContents.builder()
				.name("컨텐츠1")
				.gameScore(10)
				.gameRank(1)
				.visibleType(PUBLIC)
				.worldCupGame(worldCupGame)
				.mediaFile(mediaFile1)
				.build();
			WorldCupGameContents contents2 = WorldCupGameContents.builder()
				.name("컨텐츠2")
				.gameScore(5)
				.gameRank(2)
				.visibleType(PUBLIC)
				.worldCupGame(worldCupGame)
				.mediaFile(mediaFile2)
				.build();
			worldCupGameRepository.save(worldCupGame);
			mediaFileRepository.saveAll(of(mediaFile1, mediaFile2));
			worldCupGameContentsRepository.saveAll(of(contents1, contents2));

			// when
			List<GetWorldCupContentsResponse> response = worldCupBasedOnAuthService.getMyWorldCupGameContents(1, 1);

			// then
			GetWorldCupContentsResponse firstElement = response.get(0);
			GetWorldCupContentsResponse secondElement = response.get(1);

			assertAll(
				() -> assertThat(response.size(), is(2)),

				() -> assertThat(firstElement.contentsId(), is(1L)),
				() -> assertThat(firstElement.contentsName(), is("컨텐츠1")),
				() -> assertThat(firstElement.rank(), is(1)),
				() -> assertThat(firstElement.score(), is(10)),
				() -> assertThat(firstElement.fileResponse().mediaFileId(), is(1L)),
				() -> assertThat(firstElement.fileResponse().filePath(), is("https://www.abc.com/BS/1")),
				() -> assertThat(firstElement.fileResponse().createdAt(), is(any(LocalDateTime.class))),
				() -> assertThat(firstElement.fileResponse().updatedAt(), is(any(LocalDateTime.class))),

				() -> assertThat(secondElement.contentsId(), is(2L)),
				() -> assertThat(secondElement.contentsName(), is("컨텐츠2")),
				() -> assertThat(secondElement.rank(), is(2)),
				() -> assertThat(secondElement.score(), is(5)),
				() -> assertThat(secondElement.fileResponse().mediaFileId(), is(2L)),
				() -> assertThat(secondElement.fileResponse().filePath(), is("https://www.abc.com/BS/2")),
				() -> assertThat(secondElement.fileResponse().createdAt(), is(is(any(LocalDateTime.class)))),
				() -> assertThat(secondElement.fileResponse().updatedAt(), is(is(any(LocalDateTime.class))))
			);
		}

		@Test
		@DisplayName(EXCEPTION_PREFIX + "월드컵 게임 작성자가 아닌 것은 조회할 수 없다.")
		public void fail2() {

			// given
			WorldCupGame worldCupGame = WorldCupGame.builder()
				.title("게임1")
				.description("설명1")
				.visibleType(PUBLIC)
				.memberId(2)
				.build();

			StaticMediaFile mediaFile1 = StaticMediaFile.builder()
				.originalName("fileOriginalName")
				.objectKey("filePath")
				.extension("GIF")
				.originalFileSize("0")
				.build();

			StaticMediaFile mediaFile2 = StaticMediaFile.builder()
				.originalName("fileOriginalName")
				.objectKey("filePath")
				.extension("GIF")
				.originalFileSize("0")
				.build();

			WorldCupGameContents contents1 = WorldCupGameContents.builder()
				.name("컨텐츠1")
				.gameScore(1)
				.visibleType(PUBLIC)
				.worldCupGame(worldCupGame)
				.mediaFile(mediaFile1)
				.build();

			WorldCupGameContents contents2 = WorldCupGameContents.builder()
				.name("컨텐츠2")
				.gameScore(1)
				.visibleType(PUBLIC)
				.worldCupGame(worldCupGame)
				.mediaFile(mediaFile2)
				.build();

			worldCupGameRepository.save(worldCupGame);
			mediaFileRepository.saveAll(of(mediaFile1, mediaFile2));
			worldCupGameContentsRepository.saveAll(of(contents1, contents2));

			long worldCupId = 1;
			long memberId = 1;

			// when
			assertThrows(
				NotOwnerGameException.class,
				() -> worldCupBasedOnAuthService.getMyWorldCupGameContents(worldCupId, memberId)
			);

		}
	}

	@Nested
	@DisplayName("자신이 생성한 월드컵 목록을 조회할 수 있다.")
	public class getMyWorldCupList {

		@Test
		@DisplayName(SUCCESS_PREFIX)
		public void success() {

			var worldCupGame1 = WorldCupGame.builder()
				.description("")
				.title("title1")
				.visibleType(PUBLIC)
				.softDelete(true)
				.memberId(1)
				.build();
			var worldCupGame2 = WorldCupGame.builder()
				.description("")
				.title("title2")
				.visibleType(PUBLIC)
				.memberId(1)
				.build();
			var worldCupGame3 = WorldCupGame.builder()
				.description("")
				.title("title3")
				.visibleType(PUBLIC)
				.softDelete(true)
				.memberId(1)
				.build();
			var worldCupGame4 = WorldCupGame.builder()
				.description("")
				.title("title4")
				.visibleType(PUBLIC)
				.memberId(1)
				.build();
			var worldCupGame5 = WorldCupGame.builder()
				.description("")
				.title("title5")
				.visibleType(PUBLIC)
				.softDelete(true)
				.memberId(1)
				.build();

			worldCupGameRepository.saveAll(
				of(worldCupGame1, worldCupGame2, worldCupGame3, worldCupGame4, worldCupGame5));

			var response = worldCupBasedOnAuthService.getMyWorldCupList(1L);

			var worldCupIds = response.stream().map(GetMyWorldCupResponse::worldCupId).toList();
			assertAll(
				() -> assertThat(response.size()).isEqualTo(2),
				() -> assertThat(worldCupIds).contains(2L, 4L),
				() -> assertThat(worldCupIds).doesNotContain(1L, 3L, 5L)
			);
		}

	}

	@Nested
	@DisplayName("월드컵 게임을 생성할 수 있다.")
	public class createWorldCup {

		@Test
		@DisplayName(SUCCESS_PREFIX)
		public void success1() {

			// given
			CreateWorldCupRequest request = CreateWorldCupRequest.builder()
				.title("뱀")
				.description("사자")
				.visibleType(PRIVATE)
				.build();

			// when
			worldCupBasedOnAuthService.createMyWorldCup(request, 1L);

			// then
			WorldCupGame updatedGame = worldCupGameRepository.findById(1L).get();
			assertAll(
				() -> assertThat(updatedGame.getTitle()).isEqualTo("뱀"),
				() -> assertThat(updatedGame.getDescription()).isEqualTo("사자"),
				() -> assertThat(updatedGame.getVisibleType()).isEqualTo(PRIVATE)
			);
		}

		@Test
		@DisplayName(EXCEPTION_PREFIX + "이미 존재하는 월드컵 게임 타이틀은 사용할 수 없다.")
		public void fail2() {

			// given
			CreateWorldCupRequest request = CreateWorldCupRequest.builder()
				.title("호랑이")
				.description("뱀")
				.visibleType(PRIVATE)
				.build();

			WorldCupGame worldCupGame = WorldCupGame.builder()
				.title("호랑이")
				.description("개미")
				.visibleType(PUBLIC)
				.memberId(1)
				.build();

			worldCupGameRepository.save(worldCupGame);

			// when then
			assertThrows(
				DuplicatedWorldCupGameTitleException.class,
				() -> worldCupBasedOnAuthService.putMyWorldCup(request, 2L, 1L)
			);
		}
	}

	@Nested
	@DisplayName("월드컵 게임을 수정할 수 있다.")
	public class putWorldCup {

		@Test
		@DisplayName(SUCCESS_PREFIX)
		public void success2() {

			// given
			CreateWorldCupRequest request = CreateWorldCupRequest.builder()
				.title("원숭이")
				.description("토끼")
				.visibleType(PRIVATE)
				.build();

			WorldCupGame worldCupGame = WorldCupGame.builder()
				.title("호랑이")
				.description("개미")
				.visibleType(PUBLIC)
				.memberId(1)
				.build();

			worldCupGameRepository.save(worldCupGame);

			// when
			worldCupBasedOnAuthService.putMyWorldCup(request, 1L, 1L);

			// then
			WorldCupGame updatedGame = worldCupGameRepository.findById(1L).get();
			assertAll(
				() -> assertThat(updatedGame.getTitle()).isEqualTo("원숭이"),
				() -> assertThat(updatedGame.getDescription()).isEqualTo("토끼"),
				() -> assertThat(updatedGame.getVisibleType()).isEqualTo(PRIVATE)
			);
		}

		@Test
		@DisplayName(EXCEPTION_PREFIX + "자신이 작성한 게임이 아니면 수정할 수 없다.")
		public void fail1() {

			// given
			CreateWorldCupRequest request = CreateWorldCupRequest.builder()
				.title("원숭이")
				.description("토끼")
				.visibleType(PRIVATE)
				.build();

			WorldCupGame worldCupGame = WorldCupGame.builder()
				.title("호랑이")
				.description("개미")
				.visibleType(PUBLIC)
				.memberId(1)
				.build();

			worldCupGameRepository.save(worldCupGame);

			// when then
			assertThrows(
				NotOwnerGameException.class,
				() -> worldCupBasedOnAuthService.putMyWorldCup(request, 1L, 3L)
			);
		}

		@Test
		@DisplayName(EXCEPTION_PREFIX + "이미 존재하는 월드컵 게임 타이틀은 사용할 수 없다.")
		public void fail2() {

			// given
			CreateWorldCupRequest request = CreateWorldCupRequest.builder()
				.title("호랑이")
				.description("뱀")
				.visibleType(PRIVATE)
				.build();

			WorldCupGame worldCupGame = WorldCupGame.builder()
				.title("호랑이")
				.description("개미")
				.visibleType(PUBLIC)
				.memberId(1)
				.build();

			worldCupGameRepository.save(worldCupGame);

			// when then
			assertThrows(
				DuplicatedWorldCupGameTitleException.class,
				() -> worldCupBasedOnAuthService.putMyWorldCup(request, 2L, 1L)
			);
		}

		@Test
		@DisplayName(EXCEPTION_PREFIX + "존재하지 않는 월드컵을 수정할 수 없다.")
		public void fail3() {

			// given
			CreateWorldCupRequest request = CreateWorldCupRequest.builder()
				.title("철쭉 게임")
				.description("뱀")
				.visibleType(PRIVATE)
				.build();

			WorldCupGame worldCupGame = WorldCupGame.builder()
				.title("호랑이")
				.description("개미")
				.visibleType(PUBLIC)
				.memberId(1)
				.build();

			worldCupGameRepository.save(worldCupGame);

			// when then
			assertThrows(
				NotFoundWorldCupGameException.class,
				() -> worldCupBasedOnAuthService.putMyWorldCup(request, 2L, 1L)
			);
		}

	}

	@Nested
	@DisplayName("월드컵 게임 컨텐츠를 생성할 수 있다.")
	public class createWorldCupGameContents {

		@Test
		@DisplayName(SUCCESS_PREFIX)
		public void success1() {

			// given
			given(s3Component.putObject(anyString(), anyString()))
				.willReturn(null);

			byte[] bytes = new byte[1000 * 1000 * 4];
			new SecureRandom().nextBytes(bytes);
			var tempMediaData = Base64.getEncoder().encodeToString(bytes);

			var createStaticMediaFileContents = CreateContentsRequest.builder()
				.contentsName("컨텐츠 이름1")
				.visibleType(PUBLIC)
				.createMediaFileRequest(
					CreateMediaFileRequest.builder()
						.fileType(STATIC_MEDIA_FILE)
						.mediaData(tempMediaData)
						.originalName("Original1")
						.detailFileType("PNG")
						.build()
				)
				.build();

			var createInternetVideoUrlContents = CreateContentsRequest.builder()
				.contentsName("컨텐츠 이름2")
				.visibleType(PUBLIC)
				.createMediaFileRequest(
					CreateMediaFileRequest.builder()
						.fileType(INTERNET_VIDEO_URL)
						.mediaData("https://filepaths/2")
						.videoPlayDuration(3)
						.videoStartTime("00100")
						.detailFileType("YOU_TUBE_URL")
						.build()
				)
				.build();

			var request = builder()
				.data(of(createStaticMediaFileContents, createInternetVideoUrlContents))
				.build();

			var worldCupGame = WorldCupGame.builder()
				.title("title1")
				.visibleType(PUBLIC)
				.memberId(1)
				.build();

			worldCupGameRepository.save(worldCupGame);

			// when
			worldCupBasedOnAuthService.createMyWorldCupContents(request, 1, 1);

			// then
			List<WorldCupGameContents> contentsList = worldCupGameContentsRepository.findAll();

			StaticMediaFile firstMediaFile = (StaticMediaFile)mediaFileRepository.findById(
				contentsList.get(0).getMediaFile().getId()).get();

			InternetVideoUrl secondMediaFile = (InternetVideoUrl)mediaFileRepository.findById(
				contentsList.get(1).getMediaFile().getId()).get();

			assertAll(
				() -> assertThat(contentsList.get(0).getId()).isEqualTo(1),
				() -> assertThat(contentsList.get(0).getName()).isEqualTo("컨텐츠 이름1"),
				() -> assertThat(contentsList.get(0).getVisibleType()).isEqualTo(PUBLIC),
				() -> assertThat(contentsList.get(0).getGameRank()).isEqualTo(0),
				() -> assertThat(contentsList.get(0).getGameScore()).isEqualTo(0),

				() -> assertThat(firstMediaFile.getId()).isEqualTo(1),
				() -> assertThat(firstMediaFile.getObjectKey()).isNotNull(),
				() -> assertThat(firstMediaFile.getFileType()).isEqualTo(STATIC_MEDIA_FILE),
				() -> assertThat(firstMediaFile.getOriginalName()).isEqualTo("Original1"),
				() -> assertThat(firstMediaFile.getDetailType().name()).isEqualTo("PNG"),
				() -> assertThat(firstMediaFile.getOriginalFileSize()).isEqualTo("3"),

				() -> assertThat(contentsList.get(1).getId()).isEqualTo(2),
				() -> assertThat(contentsList.get(1).getName()).isEqualTo("컨텐츠 이름2"),
				() -> assertThat(contentsList.get(1).getVisibleType()).isEqualTo(PUBLIC),
				() -> assertThat(contentsList.get(1).getGameRank()).isEqualTo(0),
				() -> assertThat(contentsList.get(1).getGameScore()).isEqualTo(0),

				() -> assertThat(secondMediaFile.getId()).isEqualTo(2),
				() -> assertThat(secondMediaFile.getObjectKey()).isNotNull(),
				() -> assertThat(secondMediaFile.getFileType()).isEqualTo(INTERNET_VIDEO_URL),
				() -> assertThat(secondMediaFile.getVideoStartTime()).isEqualTo("00100"),
				() -> assertThat(secondMediaFile.getVideoPlayDuration()).isEqualTo(3),
				() -> assertThat(secondMediaFile.isPlayableVideo()).isEqualTo(true),
				() -> assertThat(secondMediaFile.getDetailType().name()).isEqualTo("YOU_TUBE_URL"),
				() -> assertThat(secondMediaFile.getOriginalFileSize()).isEqualTo("0")
			);
		}

		@Test
		@DisplayName(EXCEPTION_PREFIX + "존재하지 않는 월드컵에 컨텐츠를 생성할 수 없다.")
		public void fail1() {

			// given
			var createStaticMediaFileContents = CreateContentsRequest.builder()
				.contentsName("컨텐츠 이름1")
				.visibleType(PUBLIC)
				.createMediaFileRequest(
					CreateMediaFileRequest.builder()
						.fileType(STATIC_MEDIA_FILE)
						.mediaData("https://filepaths/1")
						.originalName("Original1")
						.detailFileType("GIF")
						.build()
				)
				.build();

			var createInternetVideoUrlContents = CreateContentsRequest.builder()
				.contentsName("컨텐츠 이름2")
				.visibleType(PUBLIC)
				.createMediaFileRequest(
					CreateMediaFileRequest.builder()
						.fileType(INTERNET_VIDEO_URL)
						.mediaData("https://filepaths/2")
						.videoPlayDuration(3)
						.videoStartTime("00100")
						.detailFileType("YOU_TUBE_URL")
						.build()
				)
				.build();

			var request = builder()
				.data(of(createStaticMediaFileContents, createInternetVideoUrlContents))
				.build();

			// when
			assertThrows(
				NotFoundWorldCupGameException.class,
				() -> worldCupBasedOnAuthService.createMyWorldCupContents(request, 1, 1)
			);

		}

		@Test
		@DisplayName(EXCEPTION_PREFIX + "월드컵의 주인이 아니면 컨텐츠를 생성할 수 없다.")
		public void fail2() {

			// given
			var createStaticMediaFileContents = CreateContentsRequest.builder()
				.contentsName("컨텐츠 이름1")
				.visibleType(PUBLIC)
				.createMediaFileRequest(
					CreateMediaFileRequest.builder()
						.fileType(STATIC_MEDIA_FILE)
						.mediaData("https://filepaths/1")
						.originalName("Original1")
						.detailFileType("JPG")
						.build()
				)
				.build();

			var createInternetVideoUrlContents = CreateContentsRequest.builder()
				.contentsName("컨텐츠 이름2")
				.visibleType(PUBLIC)
				.createMediaFileRequest(
					CreateMediaFileRequest.builder()
						.fileType(INTERNET_VIDEO_URL)
						.mediaData("https://filepaths/2")
						.videoPlayDuration(3)
						.videoStartTime("00100")
						.detailFileType("YOU_TUBE_URL")
						.build()
				)
				.build();

			var worldCupGame = WorldCupGame.builder()
				.title("title1")
				.visibleType(PUBLIC)
				.memberId(2)
				.build();

			worldCupGameRepository.save(worldCupGame);

			var request = builder()
				.data(of(createStaticMediaFileContents, createInternetVideoUrlContents))
				.build();

			// when
			assertThrows(
				NotOwnerGameException.class,
				() -> worldCupBasedOnAuthService.createMyWorldCupContents(request, 1, 1)
			);

		}

	}

	@Nested
	@DisplayName("자신이 생성한 월드컵 1개를 삭제할 수 있다.")
	public class deleteMyWorldCup {

		@Test
		@DisplayName(SUCCESS_PREFIX)
		public void success() {

			// given
			var worldCupGame = WorldCupGame.builder()
				.description("")
				.title("title5")
				.visibleType(PUBLIC)
				.memberId(1)
				.build();

			worldCupGameRepository.save(worldCupGame);

			worldCupBasedOnAuthService.deleteMyWorldCup(1L, 1L);

			// when
			var optionalWorldCupGame = worldCupGameRepository.findById(1L);

			// then
			assertThat(optionalWorldCupGame.isEmpty()).isTrue();
		}

	}
}
