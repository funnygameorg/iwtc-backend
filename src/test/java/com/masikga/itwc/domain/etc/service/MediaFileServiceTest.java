package com.masikga.itwc.domain.etc.service;

import static com.masikga.itwc.domain.etc.model.vo.FileType.*;
import static com.masikga.itwc.helper.TestConstant.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.masikga.itwc.domain.etc.controller.response.MediaFileResponse;
import com.masikga.itwc.domain.etc.exception.NotFoundMediaFIleException;
import com.masikga.itwc.domain.etc.model.InternetVideoUrl;
import com.masikga.itwc.domain.etc.model.StaticMediaFile;
import com.masikga.itwc.domain.etc.repository.MediaFileRepository;
import com.masikga.itwc.helper.DataBaseCleanUp;
import com.masikga.itwc.helper.testbase.IntegrationBaseTest;
import com.masikga.itwc.infra.filestorage.FileStorage;

public class MediaFileServiceTest implements IntegrationBaseTest {

	@Autowired
	private MediaFileService mediaFileService;

	@Autowired
	private MediaFileRepository mediaFileRepository;

	@MockBean
	private FileStorage fileStorage;

	@Autowired
	private DataBaseCleanUp dataBaseCleanUp;

	@BeforeEach
	public void setUp() {
		dataBaseCleanUp.truncateAllEntity();
	}

	@Nested
	@DisplayName("미디어 파일 1건을 조회할 수 있다.")
	class getMediaFile {

		@Test
		@DisplayName(SUCCESS_PREFIX + "static file 형식 미디어 파일을 조회할 수 있다.")
		public void success() throws Exception {

			// given
			StaticMediaFile staticMediaFile = StaticMediaFile.builder()
				.objectKey("A-46")
				.bucketName("bucketA")
				.originalName("originalNameA")
				.extension("GIF")
				.build();

			mediaFileRepository.save(staticMediaFile);

			given(fileStorage.getObject("A-46"))
				.willReturn("dataA");

			// when
			MediaFileResponse response = mediaFileService.getMediaFile(1L, "original");

			//then
			assertAll(
				() -> assertThat(response.mediaFileId()).isEqualTo(1),
				() -> assertThat(response.originalName()).isEqualTo("originalNameA"),
				() -> assertThat(response.mediaData()).isEqualTo("dataA"),
				() -> assertThat(response.fileType()).isEqualTo(STATIC_MEDIA_FILE),
				() -> assertThat(response.detailType()).isEqualTo("GIF"),
				() -> assertThat(response.videoPlayDuration()).isNull(),
				() -> assertThat(response.videoStartTime()).isNull()
			);

		}

		@Test
		@DisplayName(SUCCESS_PREFIX + "동영상 링크 형식 미디어 파일을 조회할 수 있다.")
		public void success2() throws Exception {

			// given
			InternetVideoUrl staticMediaFile = InternetVideoUrl.builder()
				.objectKey("A-46")
				.bucketName("bucketA")
				.videoPlayDuration(5)
				.isPlayableVideo(true)
				.videoStartTime("00000")
				.videoDetailType("YOU_TUBE_URL")
				.build();

			mediaFileRepository.save(staticMediaFile);

			given(fileStorage.getObject("A-46"))
				.willReturn("youtube_qs");

			// when
			MediaFileResponse response = mediaFileService.getMediaFile(1L, "original");

			//then
			assertAll(
				() -> assertThat(response.mediaFileId()).isEqualTo(1),
				() -> assertThat(response.videoPlayDuration()).isEqualTo(5),
				() -> assertThat(response.videoStartTime()).isEqualTo("00000"),
				() -> assertThat(response.mediaData()).isEqualTo("youtube_qs"),
				() -> assertThat(response.fileType()).isEqualTo(INTERNET_VIDEO_URL),
				() -> assertThat(response.detailType()).isEqualTo("YOU_TUBE_URL"),
				() -> assertThat(response.originalName()).isNull()
			);

		}

		@Test
		@DisplayName(SUCCESS_PREFIX + "1/2 크기의 static file 형식을 조회할 수 있다.")
		public void success3() {

			// given
			StaticMediaFile staticMediaFile = StaticMediaFile.builder()
				.objectKey("A-46")
				.bucketName("bucketA")
				.originalName("originalNameA")
				.extension("GIF")
				.build();

			mediaFileRepository.save(staticMediaFile);

			given(fileStorage.getObject("A-46", "media-divide2"))
				.willReturn("dataA");

			// when
			MediaFileResponse response = mediaFileService.getMediaFile(1L, "divide2");

			//then
			assertAll(
				() -> assertThat(response.mediaFileId()).isEqualTo(1),
				() -> assertThat(response.originalName()).isEqualTo("originalNameA"),
				() -> assertThat(response.mediaData()).isEqualTo("dataA"),
				() -> assertThat(response.fileType()).isEqualTo(STATIC_MEDIA_FILE),
				() -> assertThat(response.detailType()).isEqualTo("GIF"),
				() -> assertThat(response.videoPlayDuration()).isNull(),
				() -> assertThat(response.videoStartTime()).isNull()
			);

		}

		@Test
		@DisplayName(SUCCESS_PREFIX + "1/2 크기를 요청했지만 없다면 원본 사이즈의 static file 형식 미디어 파일이라도 조회할 수 있다.")
		public void success4() {

			// given
			StaticMediaFile staticMediaFile = StaticMediaFile.builder()
				.objectKey("A-46")
				.bucketName("bucketA")
				.originalName("originalNameA")
				.extension("GIF")
				.build();

			mediaFileRepository.save(staticMediaFile);

			given(fileStorage.getObject("A-46", "media-divide2"))
				.willThrow(new RuntimeException("S3 Client lib에서 던지는 에러"));

			given(fileStorage.getObject("A-46", "media"))
				.willReturn("dataA");

			// when
			MediaFileResponse response = mediaFileService.getMediaFile(1L, "divide2");

			//then
			assertAll(
				() -> assertThat(response.mediaFileId()).isEqualTo(1),
				() -> assertThat(response.originalName()).isEqualTo("originalNameA"),
				() -> assertThat(response.mediaData()).isEqualTo("dataA"),
				() -> assertThat(response.fileType()).isEqualTo(STATIC_MEDIA_FILE),
				() -> assertThat(response.detailType()).isEqualTo("GIF"),
				() -> assertThat(response.videoPlayDuration()).isNull(),
				() -> assertThat(response.videoStartTime()).isNull()
			);

		}

		@Test
		@DisplayName(SUCCESS_PREFIX + "1/2 크기를 요청하였으나 1/2 사이즈와  원본 사이즈 모두 없다면 null을 body로 담아서 응답한다.")
		public void success5() {

			// given
			StaticMediaFile staticMediaFile = StaticMediaFile.builder()
				.objectKey("A-46")
				.bucketName("bucketA")
				.originalName("originalNameA")
				.extension("GIF")
				.build();

			mediaFileRepository.save(staticMediaFile);

			given(fileStorage.getObject("A-46", "media-divide2"))
				.willThrow(new RuntimeException("S3 Client lib에서 던지는 에러"));

			given(fileStorage.getObject("A-46", "media"))
				.willThrow(new RuntimeException("S3 Client lib에서 던지는 에러"));

			// when
			MediaFileResponse response = mediaFileService.getMediaFile(1L, "divide2");

			//then
			assertAll(
				() -> assertThat(response.mediaFileId()).isEqualTo(1),
				() -> assertThat(response.originalName()).isEqualTo("originalNameA"),
				() -> assertThat(response.mediaData()).isEqualTo(null),
				() -> assertThat(response.fileType()).isEqualTo(STATIC_MEDIA_FILE),
				() -> assertThat(response.detailType()).isEqualTo("GIF"),
				() -> assertThat(response.videoPlayDuration()).isNull(),
				() -> assertThat(response.videoStartTime()).isNull()
			);

		}

		@Test
		@DisplayName(SUCCESS_PREFIX + "동영상 링크 형식 미디어 파일을 조회하는 경우 원본 버킷에서만 데이터를 조회합니다.")
		public void success6() {

			// given
			InternetVideoUrl videoUrl = InternetVideoUrl.builder()
				.objectKey("A-46")
				.bucketName("bucketA")
				.videoPlayDuration(5)
				.isPlayableVideo(true)
				.videoStartTime("00000")
				.videoDetailType("YOU_TUBE_URL")
				.build();

			mediaFileRepository.save(videoUrl);

			given(fileStorage.getObject("A-46"))
				.willReturn("youtube_qs");

			// when
			MediaFileResponse response = mediaFileService.getMediaFile(1L, "divide2");

			//then
			assertAll(
				() -> assertThat(response.mediaFileId()).isEqualTo(1),
				() -> assertThat(response.videoPlayDuration()).isEqualTo(5),
				() -> assertThat(response.videoStartTime()).isEqualTo("00000"),
				() -> assertThat(response.mediaData()).isEqualTo("youtube_qs"),
				() -> assertThat(response.fileType()).isEqualTo(INTERNET_VIDEO_URL),
				() -> assertThat(response.detailType()).isEqualTo("YOU_TUBE_URL"),
				() -> assertThat(response.originalName()).isNull()
			);

		}

		@Test
		@DisplayName(EXCEPTION_PREFIX + "존재하지 않는 미디어 파일은 조회할 수 없다.")
		public void fail() {

			// when then
			assertThrows(
				NotFoundMediaFIleException.class,
				() -> mediaFileService.getMediaFile(1L, "original")
			);

		}
	}

}
