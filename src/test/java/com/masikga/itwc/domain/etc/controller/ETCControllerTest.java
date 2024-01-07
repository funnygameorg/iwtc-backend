package com.masikga.itwc.domain.etc.controller;

import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.masikga.itwc.domain.etc.controller.request.WriteCommentRequest;
import com.masikga.itwc.helper.testbase.WebMvcBaseTest;
import com.masikga.itwc.helper.TestConstant;

public class ETCControllerTest extends WebMvcBaseTest {

	private static final String GET_COMMENTS_LIST_API = ROOT_PATH + "/world-cups/{worldCupId}/comments";
	private static final String WRITE_COMMENT_API =
		ROOT_PATH + "/world-cups/{worldCupId}/contents/{contentsId}/comments";
	private static final String DELETE_COMMENT_API = ROOT_PATH + "/comments/{commentId}";

	@Test
	@DisplayName(TestConstant.SUCCESS_PREFIX + "월드컵의 댓글 리스트 조회")
	public void getCommentList() throws Exception {

		mockMvc.perform(
				get(GET_COMMENTS_LIST_API, 1L)
					.param("offset", "0")
			)
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName(TestConstant.SUCCESS_PREFIX + "월드컵의 댓글 작성")
	public void writeComment() throws Exception {

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

	@Test
	@DisplayName(TestConstant.SUCCESS_PREFIX + "월드컵의 댓글 삭제")
	public void deleteComment() throws Exception {

		mockMvc.perform(
				delete(DELETE_COMMENT_API, 1L, 1L)
			)
			.andExpect(status().isNoContent());
	}

}
