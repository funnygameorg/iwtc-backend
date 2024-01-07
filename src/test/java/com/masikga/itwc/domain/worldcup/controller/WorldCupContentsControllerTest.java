package com.masikga.itwc.domain.worldcup.controller;

import static com.masikga.itwc.helper.TestConstant.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.masikga.itwc.domain.worldcup.controller.request.ClearWorldCupGameRequest;
import com.masikga.itwc.helper.testbase.WebMvcBaseTest;

public class WorldCupContentsControllerTest extends WebMvcBaseTest {
	private final static String GET_AVAILABLE_ROUNDS_API = WORLD_CUPS_PATH + "/{worldCupId}/available-rounds";
	private final static String CLEAR_WORLD_CUP_GAME_API = WORLD_CUPS_PATH + "/{worldCupId}/clear";

	@Test
	@DisplayName(SUCCESS_PREFIX + "플레이 가능한 월드컵 게임 라운드 수 조회 요청 검증")
	public void getAvailableRoundsApi() throws Exception {
		mockMvc.perform(
			get(GET_AVAILABLE_ROUNDS_API, 1L)
		).andExpect(
			status().isOk()
		);
	}

	@Test
	@DisplayName(SUCCESS_PREFIX + "이상형 월드컵 게임 종료 요청 검증")
	public void clearWorldCupGame() throws Exception {
		ClearWorldCupGameRequest request = ClearWorldCupGameRequest
			.builder()
			.firstWinnerContentsId(1)
			.secondWinnerContentsId(2)
			.thirdWinnerContentsId(3)
			.fourthWinnerContentsId(4)
			.build();
		int worldCupGameId = 1;

		mockMvc.perform(
				post(CLEAR_WORLD_CUP_GAME_API, worldCupGameId)
					.content(objectMapper.writeValueAsString(request))
					.contentType(APPLICATION_JSON)
			)
			.andExpect(
				status().isCreated()
			);
	}

}
