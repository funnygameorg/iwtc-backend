package com.example.demo.worldcup.model;

import static com.example.demo.domain.worldcup.model.vo.VisibleType.*;
import static com.example.demo.helper.TestConstant.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.example.demo.common.error.exception.NotNullArgumentException;
import com.example.demo.domain.etc.model.InternetVideoUrl;
import com.example.demo.domain.etc.model.StaticMediaFile;
import com.example.demo.domain.worldcup.model.WorldCupGameContents;

public class WorldCupGameContentsTest {

	@Nested
	@DisplayName("사용자 컨텐츠 관리페이지에서 보낸 수정 데이터로 컨텐츠를 업데이트할 수 있다.")
	class UpdateByCommonManage {

		@Test
		@DisplayName(SUCCESS_PREFIX + "정적 파일 컨텐츠 수정")
		public void success1() {

			StaticMediaFile staticMediaFile = StaticMediaFile.builder()
				.originalName("붕어빵.JPG")
				.extension("jpg")
				.objectKey("ABC-9910")
				.bucketName("IMAGES")
				.build();

			WorldCupGameContents contents = WorldCupGameContents.builder()
				.name("붕어빵")
				.visibleType(PUBLIC)
				.gameRank(1)
				.gameScore(10)
				.mediaFile(staticMediaFile)
				.build();

			// when
			contents.updateByCommonManage(
				"초코빵 컨텐츠",
				"choco!",
				null,
				null,
				PUBLIC,
				"CDA-1212"
			);

			Assertions.assertAll(
				() -> assertThat(contents.getName()).isEqualTo("초코빵 컨텐츠"),
				() -> assertThat(contents.getVisibleType()).isEqualTo(PUBLIC),
				() -> assertThat(contents.getName()).isEqualTo("초코빵 컨텐츠"),
				() -> assertThat(staticMediaFile.getOriginalName()).isEqualTo("choco!"),
				() -> assertThat(staticMediaFile.getObjectKey()).isEqualTo("CDA-1212")
			);
		}

		@Test
		@DisplayName(SUCCESS_PREFIX + "인터넷 비디오 컨텐츠 수정")
		public void success2() {

			InternetVideoUrl internetVideoUrl = InternetVideoUrl.builder()
				.objectKey("AZD-908")
				.videoStartTime("10100")
				.videoPlayDuration(5)
				.isPlayableVideo(true)
				.build();

			WorldCupGameContents contents = WorldCupGameContents.builder()
				.name("잔잔한 음악 OST, 백색 소음 64개 1시간 [잔잔, 무드, 고요...]")
				.visibleType(PUBLIC)
				.gameRank(1)
				.gameScore(10)
				.mediaFile(internetVideoUrl)
				.build();

			// when
			contents.updateByCommonManage(
				"시끄러운 음악 OST, play music 2시간",
				null,
				"00105",
				3,
				PUBLIC,
				"ZZQ-1212"
			);

			Assertions.assertAll(
				() -> assertThat(contents.getName()).isEqualTo("시끄러운 음악 OST, play music 2시간"),
				() -> assertThat(contents.getVisibleType()).isEqualTo(PUBLIC),
				() -> assertThat(internetVideoUrl.getVideoStartTime()).isEqualTo("00105"),
				() -> assertThat(internetVideoUrl.getVideoPlayDuration()).isEqualTo(3),
				() -> assertThat(internetVideoUrl.getObjectKey()).isEqualTo("ZZQ-1212")
			);
		}

		@Test
		@DisplayName(EXCEPTION_PREFIX + "정적 파일 컨텐츠 수정에서 `원래 파일명`, `S3 식별자`가 없으면 안된다.")
		public void fail1() {

			StaticMediaFile staticMediaFile = StaticMediaFile.builder()
				.originalName("붕어빵.JPG")
				.extension("jpg")
				.objectKey("ABC-9910")
				.bucketName("IMAGES")
				.build();

			WorldCupGameContents contents = WorldCupGameContents.builder()
				.name("잔잔한 음악 OST, 백색 소음 64개 1시간 [잔잔, 무드, 고요...]")
				.visibleType(PUBLIC)
				.gameRank(1)
				.gameScore(10)
				.mediaFile(staticMediaFile)
				.build();

			// when & then
			assertThrows(
				NotNullArgumentException.class,
				() -> contents.updateByCommonManage(
					"새로운 정적 파일",
					null,
					null,
					null,
					PUBLIC,
					null
				)
			);
		}

		@Test
		@DisplayName(EXCEPTION_PREFIX + "인터넷 비디오 컨텐츠 수정에서 `영상 시작 시간`, `반복 구간`, `S3 식별자`가 없으면 안된다.")
		public void fail2() {

			InternetVideoUrl internetVideoUrl = InternetVideoUrl.builder()
				.objectKey("AZD-908")
				.videoStartTime("00100")
				.videoPlayDuration(5)
				.isPlayableVideo(true)
				.build();

			WorldCupGameContents contents = WorldCupGameContents.builder()
				.name("잔잔한 음악 OST, 백색 소음 64개 1시간 [잔잔, 무드, 고요...]")
				.visibleType(PUBLIC)
				.gameRank(1)
				.gameScore(10)
				.mediaFile(internetVideoUrl)
				.build();

			// when & then
			assertThrows(
				NotNullArgumentException.class,
				() -> contents.updateByCommonManage(
					"시끄러운 음악 OST, play music 2시간",
					null,
					null,
					null,
					PUBLIC,
					null
				)
			);
		}

	}
}
