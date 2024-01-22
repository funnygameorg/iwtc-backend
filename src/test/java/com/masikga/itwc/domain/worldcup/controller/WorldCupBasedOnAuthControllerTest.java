package com.masikga.itwc.domain.worldcup.controller;

import static com.masikga.itwc.domain.worldcup.model.vo.VisibleType.*;
import static com.masikga.itwc.helper.TestConstant.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.masikga.itwc.domain.etc.model.vo.FileType;
import com.masikga.itwc.domain.worldcup.controller.request.CreateWorldCupContentsRequest;
import com.masikga.itwc.domain.worldcup.controller.request.CreateWorldCupContentsRequest.CreateMediaFileRequest;
import com.masikga.itwc.domain.worldcup.controller.request.CreateWorldCupRequest;
import com.masikga.itwc.domain.worldcup.controller.request.UpdateWorldCupContentsRequest;
import com.masikga.itwc.helper.testbase.WebMvcBaseTest;

public class WorldCupBasedOnAuthControllerTest extends WebMvcBaseTest {

	private final static String GET_MY_GAME_CONTENTS_API = WORLD_CUPS_PATH + "/me/{worldCupId}/contents";
	private final static String UPDATE_MY_WORLD_CUP = WORLD_CUPS_PATH + "/me/{worldCupId}/contents/{contentsId}";
	private final static String CREATE_MY_WORLD_CUP = WORLD_CUPS_PATH + "/me";

	private final static String CREATE_MY_WORLD_CUP_CONTENTS = WORLD_CUPS_PATH + "/me/{worldCupId}/contents";

	@Nested
	@DisplayName("사용자는 월드컵 게임에 등록된 후보리스트를 볼 수 있다.")
	public class getMyGameContents {

		@Test
		@DisplayName(SUCCESS_PREFIX)
		public void success() throws Exception {

			mockMvc.perform(
					get(GET_MY_GAME_CONTENTS_API, 1))
				.andExpect(status().isOk());

		}

	}

	@Nested
	@DisplayName("사용자는 월드컵 컨텐츠 1건을 수정할 수 있다.")
	public class putWorldCup {

		@Test
		@DisplayName(SUCCESS_PREFIX)
		public void success() throws Exception {

			var request = UpdateWorldCupContentsRequest.builder()
				.contentsName("ABCDEFG")
				.originalName("ABCDEFG")
				.mediaData("ABCDEFAS")
				.detailFileType("GIF")
				.videoPlayDuration(5)
				.videoPlayDuration(5)
				.visibleType(PUBLIC)
				.build();

			mockMvc.perform(
					put(UPDATE_MY_WORLD_CUP, 1, 1)
						.content(objectMapper.writeValueAsString(request))
						.contentType(APPLICATION_JSON)
				)
				.andExpect(status().isNoContent())
				.andExpect(jsonPath("$.message").value("게임 수정"));

		}

		@ParameterizedTest
		@NullAndEmptySource
		@DisplayName(EXCEPTION_PREFIX + "컨텐츠 이름은 필수다.")
		public void fail1(String contentsName) throws Exception {

			var request = UpdateWorldCupContentsRequest.builder()
				.contentsName(contentsName)
				.originalName("ABCDEFG")
				.mediaData("ABCDEFAS")
				.detailFileType("PNG")
				.videoPlayDuration(5)
				.visibleType(PUBLIC)
				.build();

			mockMvc.perform(
					put(UPDATE_MY_WORLD_CUP, 1, 1)
						.content(objectMapper.writeValueAsString(request))
						.contentType(APPLICATION_JSON)
				)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("[월드컵 컨텐츠 이름: 필수]"));

		}

		@ParameterizedTest
		@ValueSource(strings = {
			"MP4",
			"WEBP"
		})
		@DisplayName(EXCEPTION_PREFIX + "정해진 미디어 파일 형식으로만 요청할 수 있다.")
		public void fail2(String detailFileType) throws Exception {

			var request = UpdateWorldCupContentsRequest.builder()
				.contentsName("ABCDEFG")
				.originalName("ABCDEFG")
				.mediaData("ABCDEFAS")
				.detailFileType(detailFileType)
				.videoPlayDuration(5)
				.visibleType(PUBLIC)
				.build();

			mockMvc.perform(
					put(UPDATE_MY_WORLD_CUP, 1, 1)
						.content(objectMapper.writeValueAsString(request))
						.contentType(APPLICATION_JSON)
				)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("[허용 미디어 파일 형식 : GIF, PNG, JPEG, JPG, YOU_TUBE_URL]"));

		}

	}

	@Nested
	@DisplayName("월드컵 게임을 생성할 수 있다.")
	public class createMyWorldCup {

		@Test
		@DisplayName(SUCCESS_PREFIX)
		public void success() throws Exception {

			var request = CreateWorldCupRequest.builder()
				.title("월드컵")
				.description("디스크립션")
				.visibleType(PRIVATE)
				.build();

			mockMvc.perform(
					post(CREATE_MY_WORLD_CUP)
						.content(objectMapper.writeValueAsString(request))
						.contentType(APPLICATION_JSON)
				)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.message").value("게임 생성"));

		}

		@Test
		@DisplayName(EXCEPTION_PREFIX + "월드컵 제목은 필수입니다.")
		public void fail() throws Exception {

			CreateWorldCupRequest request = CreateWorldCupRequest.builder()
				.title(null)
				.description("디스크립션 입니다.")
				.visibleType(PRIVATE)
				.build();

			mockMvc.perform(
					post(CREATE_MY_WORLD_CUP)
						.content(objectMapper.writeValueAsString(request))
						.contentType(APPLICATION_JSON)
				)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("[월드컵 이름: 필수]"));

		}

		@Test
		@DisplayName(EXCEPTION_PREFIX + "월드컵 설명은 최대 100자 입니다.")
		public void fail2() throws Exception {

			var description = """
				ABCDEFGHIJ
				ABCDEFGHIJ
				ABCDEFGHIJ
				ABCDEFGHIJ
				ABCDEFGHIJ
				ABCDEFGHIJ
				ABCDEFGHIJ
				ABCDEFGHIJ
				ABCDEFGHIJAA
				""";
			CreateWorldCupRequest request = CreateWorldCupRequest.builder()
				.title("월드컵 제목")
				.description(description)
				.visibleType(PRIVATE)
				.build();

			mockMvc.perform(
					post(CREATE_MY_WORLD_CUP)
						.content(objectMapper.writeValueAsString(request))
						.contentType(APPLICATION_JSON)
				)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("[월드컵 내용 : 최대 100]"));

		}

	}

	@Nested
	@DisplayName("월드컵 컨텐츠 1건 이상 생성")
	public class createMyWorldCupContents {

		@Test
		@DisplayName(SUCCESS_PREFIX)
		public void success() throws Exception {

			var createStaticMediaFileContents =
				CreateWorldCupContentsRequest.CreateContentsRequest.builder()
					.contentsName("컨텐츠 네임1")
					.visibleType(PUBLIC)
					.createMediaFileRequest(
						CreateMediaFileRequest.builder()
							.fileType(FileType.INTERNET_VIDEO_URL)
							.mediaData("https://www.naver.com/apples/13")
							.videoPlayDuration(3)
							.videoStartTime("00011")
							.detailFileType("YOU_TUBE_URL")
							.build()
					)
					.build();

			var createInternetVideoUrlContents =
				CreateWorldCupContentsRequest.CreateContentsRequest.builder()
					.contentsName("컨텐츠 네임1")
					.visibleType(PUBLIC)
					.createMediaFileRequest(
						CreateMediaFileRequest.builder()
							.fileType(FileType.INTERNET_VIDEO_URL)
							.mediaData("https://www.naver.com/apples/13")
							.videoPlayDuration(3)
							.videoStartTime("00011")
							.detailFileType("PNG")
							.build()
					)
					.build();

			var request = CreateWorldCupContentsRequest.builder()
				.data(List.of(createStaticMediaFileContents, createInternetVideoUrlContents))
				.build();

			mockMvc.perform(
					post(CREATE_MY_WORLD_CUP_CONTENTS, 1L)
						.content(objectMapper.writeValueAsBytes(request))
						.contentType(APPLICATION_JSON)
				)
				.andExpect(status().isCreated());

		}

		@Test
		@DisplayName(EXCEPTION_PREFIX + "컨텐츠의 이름은 필수값이다.")
		public void fail() throws Exception {

			var createStaticMediaFileContents =
				CreateWorldCupContentsRequest.CreateContentsRequest.builder()
					.contentsName("컨텐츠 네임1")
					.visibleType(PUBLIC)
					.createMediaFileRequest(
						CreateMediaFileRequest.builder()
							.fileType(FileType.INTERNET_VIDEO_URL)
							.mediaData("https://www.naver.com/apples/13")
							.videoPlayDuration(3)
							.videoStartTime("00011")
							.detailFileType("YOU_TUBE_URL")
							.build()
					)
					.build();

			var createInternetVideoUrlContents =
				CreateWorldCupContentsRequest.CreateContentsRequest.builder()
					.contentsName(null)
					.visibleType(PUBLIC)
					.createMediaFileRequest(
						CreateMediaFileRequest.builder()
							.fileType(FileType.INTERNET_VIDEO_URL)
							.mediaData("https://www.naver.com/apples/13")
							.videoPlayDuration(3)
							.videoStartTime("00011")
							.detailFileType("PNG")
							.build()
					)
					.build();

			var request = CreateWorldCupContentsRequest.builder()
				.data(List.of(createStaticMediaFileContents, createInternetVideoUrlContents))
				.build();

			mockMvc.perform(
					post(CREATE_MY_WORLD_CUP_CONTENTS, 1L)
						.content(objectMapper.writeValueAsBytes(request))
						.contentType(APPLICATION_JSON)
				)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("[월드컵 컨텐츠 이름: 필수]"));

		}

		@Test
		@DisplayName(EXCEPTION_PREFIX + "허용 미디어 파일 형식에서만 요청할 수 있습니다.")
		public void fail2() throws Exception {

			var createStaticMediaFileContents =
				CreateWorldCupContentsRequest.CreateContentsRequest.builder()
					.contentsName("컨텐츠 네임1")
					.visibleType(PUBLIC)
					.createMediaFileRequest(
						CreateMediaFileRequest.builder()
							.fileType(FileType.INTERNET_VIDEO_URL)
							.mediaData("https://www.naver.com/apples/13")
							.videoPlayDuration(3)
							.videoStartTime("00011")
							.detailFileType("YOU_TUBE_LINK")
							.build()
					)
					.build();

			var createInternetVideoUrlContents =
				CreateWorldCupContentsRequest.CreateContentsRequest.builder()
					.contentsName(null)
					.visibleType(PUBLIC)
					.createMediaFileRequest(
						CreateMediaFileRequest.builder()
							.fileType(FileType.INTERNET_VIDEO_URL)
							.mediaData("https://www.naver.com/apples/13")
							.videoPlayDuration(3)
							.videoStartTime("00011")
							.detailFileType("PNG")
							.build()
					)
					.build();

			var request = CreateWorldCupContentsRequest.builder()
				.data(List.of(createStaticMediaFileContents, createInternetVideoUrlContents))
				.build();

			mockMvc.perform(
					post(CREATE_MY_WORLD_CUP_CONTENTS, 1L)
						.content(objectMapper.writeValueAsBytes(request))
						.contentType(APPLICATION_JSON)
				)
				.andExpect(status().isBadRequest())
				.andExpect(
					jsonPath("$.message").value("[월드컵 컨텐츠 이름: 필수, 허용 미디어 파일 형식 : GIF, PNG, JPEG, JPG, YOU_TUBE_URL]"));

		}

		@Test
		@DisplayName(EXCEPTION_PREFIX + "미디어 데이터 파일은 필수입니다.")
		public void fail3() throws Exception {

			var createStaticMediaFileContents =
				CreateWorldCupContentsRequest.CreateContentsRequest.builder()
					.contentsName("컨텐츠 네임1")
					.visibleType(PUBLIC)
					.createMediaFileRequest(
						CreateMediaFileRequest.builder()
							.fileType(FileType.INTERNET_VIDEO_URL)
							.mediaData(null)
							.videoPlayDuration(3)
							.videoStartTime("00011")
							.detailFileType("YOU_TUBE_URL")
							.build()
					)
					.build();

			var createInternetVideoUrlContents =
				CreateWorldCupContentsRequest.CreateContentsRequest.builder()
					.contentsName("컨텐츠 네임2")
					.visibleType(PUBLIC)
					.createMediaFileRequest(
						CreateMediaFileRequest.builder()
							.fileType(FileType.INTERNET_VIDEO_URL)
							.mediaData("https://www.naver.com/apples/13")
							.videoPlayDuration(3)
							.videoStartTime("00011")
							.detailFileType("PNG")
							.build()
					)
					.build();

			var request = CreateWorldCupContentsRequest.builder()
				.data(List.of(createStaticMediaFileContents, createInternetVideoUrlContents))
				.build();

			mockMvc.perform(
					post(CREATE_MY_WORLD_CUP_CONTENTS, 1L)
						.content(objectMapper.writeValueAsBytes(request))
						.contentType(APPLICATION_JSON)
				)
				.andExpect(status().isBadRequest())
				.andExpect(
					jsonPath("$.message").value("[월드컵 컨텐츠 파일 이름: 필수 값]"));

		}

	}

}
