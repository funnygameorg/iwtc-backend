package com.example.demo.domain.worldcup.controller;

import static com.example.demo.domain.worldcup.model.vo.VisibleType.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.demo.domain.etc.model.vo.FileType;
import com.example.demo.domain.worldcup.controller.request.CreateWorldCupContentsRequest;
import com.example.demo.domain.worldcup.controller.request.CreateWorldCupContentsRequest.CreateMediaFileRequest;
import com.example.demo.domain.worldcup.controller.request.CreateWorldCupRequest;
import com.example.demo.helper.testbase.WebMvcBaseTest;

public class WorldCupBasedOnAuthControllerTest extends WebMvcBaseTest {

	private final static String GET_MY_GAME_CONTENTS_API = WORLD_CUPS_PATH + "/me/{worldCupId}/contents";
	private final static String UPDATE_MY_WORLD_CUP = WORLD_CUPS_PATH + "/me/{worldCupId}";
	private final static String CREATE_MY_WORLD_CUP = WORLD_CUPS_PATH + "/me";

	private final static String CREATE_MY_WORLD_CUP_CONTENTS = WORLD_CUPS_PATH + "/me/{worldCupId}/contents";

	@Test
	@DisplayName("사용자는 월드컵 게임에 등록된 후보리스트를 볼 수 있다.")
	public void getMyGameContents() throws Exception {

		mockMvc.perform(
				get(GET_MY_GAME_CONTENTS_API, 1))
			.andExpect(status().isOk());

	}

	@Test
	@DisplayName("사용자는 월드컵 게임을 수정할 수 있다.")
	public void putWorldCup1() throws Exception {

		CreateWorldCupRequest request = CreateWorldCupRequest.builder()
			.title("월드컵")
			.description("디스크립션")
			.visibleType(PRIVATE)
			.build();

		mockMvc.perform(
				put(UPDATE_MY_WORLD_CUP, 1)
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
			)
			.andExpect(status().isNoContent());

	}

	@Test
	@DisplayName("사용자는 월드컵 게임을 생성할 수 있다.")
	public void putWorldCup2() throws Exception {

		CreateWorldCupRequest request = CreateWorldCupRequest.builder()
			.title("월드컵")
			.description("디스크립션")
			.visibleType(PRIVATE)
			.build();

		mockMvc.perform(
				post(CREATE_MY_WORLD_CUP)
					.content(objectMapper.writeValueAsBytes(request))
					.contentType(APPLICATION_JSON)
			)
			.andExpect(status().isCreated());

	}

	@Test
	@DisplayName("사용자는 월드컵 게임 컨텐츠를 생성할 수 있다.")
	public void putWorldCupContents1() throws Exception {

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

}
