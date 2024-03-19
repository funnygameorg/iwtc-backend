package com.masikga.member.infra.filestorage;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.masikga.member.helper.testbase.IntegrationBaseTest;
import com.masikga.worldcupgame.infra.filestorage.FileStorage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static com.masikga.member.helper.TestConstant.EXCEPTION_PREFIX;
import static com.masikga.member.helper.TestConstant.SUCCESS_PREFIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

class FileStorageTest implements IntegrationBaseTest {

	@Autowired
	private FileStorage fileStorage;
	@MockBean
	private AmazonS3Client amazonS3Client;

	@Nested
	@DisplayName("저장소에서 파일을 조회할 수 있다.")
	public class getObject {

		@Test
		@DisplayName(SUCCESS_PREFIX)
		public void success() throws Exception {

			// given
			var returnObject = new S3Object();
			InputStream is = new ByteArrayInputStream("temp_text".getBytes());
			returnObject.setObjectContent(is);

			given(amazonS3Client.getObject("media", "object_key")).willReturn(returnObject);

			// when
			var result = fileStorage.getObject("object_key");

			// then
			assertThat(result).isEqualTo("temp_text");
		}

		@Test
		@DisplayName(EXCEPTION_PREFIX + "존재하지 않는 오브젝트를 조회할 수 없다.")
		public void fail() {

			// when
			assertThrows(
				RuntimeException.class,
				() -> fileStorage.getObject("object_key")
			);
		}
	}
}