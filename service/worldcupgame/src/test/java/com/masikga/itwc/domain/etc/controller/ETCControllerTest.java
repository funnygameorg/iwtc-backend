package com.masikga.itwc.domain.etc.controller;

import com.masikga.itwc.domain.etc.controller.request.WriteCommentRequest;
import com.masikga.itwc.helper.testbase.WebMvcBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.masikga.itwc.helper.TestConstant.EXCEPTION_PREFIX;
import static com.masikga.itwc.helper.TestConstant.SUCCESS_PREFIX;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ETCControllerTest extends WebMvcBaseTest {

	private static final String GET_COMMENTS_LIST_API = ROOT_PATH + "/world-cups/{worldCupId}/comments";
	private static final String WRITE_COMMENT_API =
		ROOT_PATH + "/world-cups/{worldCupId}/contents/{contentsId}/comments";
	private static final String DELETE_COMMENT_API = ROOT_PATH + "/comments/{commentId}";

	private static final String GET_MEDIA_FILE_API = ROOT_PATH + "/media-files/{mediaFileId}";

	@Nested
	@DisplayName("미디어 파일 조회 테스트")
	public class getMediaFiles {

		@ParameterizedTest
		@ValueSource(strings = {"original", "divide2"})
		@NullAndEmptySource
		@DisplayName(SUCCESS_PREFIX)
		public void success(String size) throws Exception {

			mockMvc.perform(
					get(GET_MEDIA_FILE_API, 1L)
						.param("size", size)
				)
				.andExpect(status().isOk());
		}

		@ParameterizedTest
		@ValueSource(strings = {"origin", "1/2", "double"})
		@DisplayName(EXCEPTION_PREFIX + " 존재하지 않는 사이즈로는 미디어 파일을 요청할 수 없다. ")
		public void fail(String size) throws Exception {

			mockMvc.perform(
					get(GET_MEDIA_FILE_API, 1L)
						.param("size", size)
				)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("[original(원본), dvide2(1/2 사이즈)만 지원합니다.]"));
		}

	}

	@Nested
	@DisplayName("월드컵 컨텐츠 목록 조회 테스트")
	public class getComments {

		@Test
		@DisplayName(SUCCESS_PREFIX + "월드컵의 댓글 리스트 조회")
		public void getCommentList() throws Exception {

			mockMvc.perform(
					get(GET_COMMENTS_LIST_API, 1L)
						.param("offset", "0")
				)
				.andExpect(status().isOk());
		}

	}

	@Nested
	@DisplayName("월드컵 컨텐츠에 댓글 작성 테스트")
	public class writeComment {

		@Test
		@DisplayName(SUCCESS_PREFIX)
		public void success() throws Exception {

			var request = WriteCommentRequest.builder()
				.body("ABC")
				.nickname("손님1")
				.build();

			mockMvc.perform(
					post(WRITE_COMMENT_API, 1L, 1L)
						.content(objectMapper.writeValueAsString(request))
						.contentType(APPLICATION_JSON)
				)
				.andExpect(status().isCreated());
		}

		@ParameterizedTest
		@ValueSource(strings = {"", "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDE"})
		@DisplayName(EXCEPTION_PREFIX + "댓글 내용은 1 ~ 30글자여야 한다.")
		public void fail(String body) throws Exception {

			var request = WriteCommentRequest.builder()
				.body(body)
				.nickname("손님1")
				.build();

			mockMvc.perform(
					post(WRITE_COMMENT_API, 1L, 1L)
						.content(objectMapper.writeValueAsString(request))
						.contentType(APPLICATION_JSON)
				)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errorCode").value(0))
				.andExpect(jsonPath("$.message").value("[댓글 내용 1 ~ 30자]"));
		}

		@Test
		@DisplayName(EXCEPTION_PREFIX + "댓글 내용은 `null`일 수 없다.")
		public void fail2() throws Exception {

			var request = WriteCommentRequest.builder()
				.body(null)
				.nickname("손님1")
				.build();

			mockMvc.perform(
					post(WRITE_COMMENT_API, 1L, 1L)
						.content(objectMapper.writeValueAsString(request))
						.contentType(APPLICATION_JSON)
				)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errorCode").value(0))
				.andExpect(jsonPath("$.message").value("[데이터가 존재하지 않습니다.(not null)]"));
		}

	}

	@Nested
	@DisplayName("월드컵 컨텐츠 댓글 삭제 테스트")
	public class deleteComment {

		@Test
		@DisplayName(SUCCESS_PREFIX + "월드컵의 댓글 삭제")
		public void success() throws Exception {

			mockMvc.perform(
					delete(DELETE_COMMENT_API, 1L, 1L)
				)
				.andExpect(status().isNoContent());
		}

	}

}
