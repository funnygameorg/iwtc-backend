package com.masikga.itwc.domain.etc.service;

import static com.masikga.itwc.domain.etc.model.vo.FileType.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.masikga.itwc.domain.etc.controller.response.MediaFileResponse;
import com.masikga.itwc.domain.etc.model.StaticMediaFile;
import com.masikga.itwc.domain.etc.repository.MediaFileRepository;
import com.masikga.itwc.helper.DataBaseCleanUp;
import com.masikga.itwc.helper.testbase.IntegrationBaseTest;
import com.masikga.itwc.infra.filestorage.S3Component;
import com.masikga.itwc.helper.TestConstant;

public class MediaFileServiceTest implements IntegrationBaseTest {

	@Autowired
	private MediaFileService mediaFileService;

	@Autowired
	private MediaFileRepository mediaFileRepository;

	@MockBean
	private S3Component s3Component;

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
		@DisplayName(TestConstant.SUCCESS_PREFIX)
		public void success() throws IOException {

			// given
			StaticMediaFile staticMediaFile = StaticMediaFile.builder()
				.objectKey("A-46")
				.bucketName("bucketA")
				.originalName("originalNameA")
				.extension(".png")
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
				() -> assertThat(response.videoPlayDuration()).isNull(),
				() -> assertThat(response.videoStartTime()).isNull()
			);

		}
	}

}
