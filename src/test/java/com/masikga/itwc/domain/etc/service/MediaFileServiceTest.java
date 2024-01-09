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
	private FileStorage s3Component;

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
		@DisplayName(SUCCESS_PREFIX + "static file 형식 조회")
		public void success() {

			// given
			StaticMediaFile staticMediaFile = StaticMediaFile.builder()
				.objectKey("A-46")
				.bucketName("bucketA")
				.originalName("originalNameA")
				.extension("GIF")
				.build();

			mediaFileRepository.save(staticMediaFile);

			given(s3Component.getObject("A-46"))
				.willReturn("dataA");

			// when
			MediaFileResponse response = mediaFileService.getMediaFile(1L);

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
		@DisplayName(SUCCESS_PREFIX + "동영상 링크 형식 조회")
		public void success2() {

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

			given(s3Component.getObject("A-46"))
				.willReturn("youtube_qs");

			// when
			MediaFileResponse response = mediaFileService.getMediaFile(1L);

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
				() -> mediaFileService.getMediaFile(1L)
			);

		}
	}

}
