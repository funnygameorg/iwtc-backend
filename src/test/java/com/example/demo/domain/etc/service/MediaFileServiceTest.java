package com.example.demo.domain.etc.service;

import static com.example.demo.domain.etc.model.vo.FileType.*;
import static com.example.demo.helper.TestConstant.*;
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

import com.example.demo.domain.etc.controller.response.MediaFileResponse;
import com.example.demo.domain.etc.model.StaticMediaFile;
import com.example.demo.domain.etc.repository.MediaFileRepository;
import com.example.demo.helper.DataBaseCleanUp;
import com.example.demo.helper.testbase.IntegrationBaseTest;
import com.example.demo.infra.filestorage.S3Component;

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
		@DisplayName(SUCCESS_PREFIX)
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
