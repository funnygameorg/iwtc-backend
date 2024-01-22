package com.masikga.itwc.domain.worldcup.controller;

import static com.masikga.itwc.helper.TestConstant.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.masikga.itwc.domain.worldcup.controller.vo.WorldCupDateRange;
import com.masikga.itwc.helper.testbase.WebMvcBaseTest;

public class WorldCupGameControllerTest extends WebMvcBaseTest {

	@Nested
	@DisplayName("월드컵 리스트 조회 요청 검증")
	public class getPagingWorldCUpGameApi {

		@Test
		@DisplayName(SUCCESS_PREFIX)
		public void success1() throws Exception {
			WorldCupDateRange dateRange = WorldCupDateRange.MONTH;
			mockMvc.perform(
					get(WORLD_CUPS_PATH)
						.queryParam("dateRange", dateRange.name())
				)
				.andExpect(status().isOk());
		}

		@ParameterizedTest
		@CsvSource(value = {
			"AAAAA",
			"A ",
			"123",
			"Θ\nΘ\n"}
		)
		@DisplayName(EXCEPTION_PREFIX + "dateRange : Date 포맷이 아님")
		public void fail1(String dateRange) throws Exception {
			mockMvc.perform(
					get(WORLD_CUPS_PATH)
						.queryParam("dateRange", dateRange)
				)
				.andExpect(status().is5xxServerError());
		}

		@Test
		@DisplayName(EXCEPTION_PREFIX + " keyword : 길이 11개 초과 불가")
		public void fail2() throws Exception {

			var keyword = "aaaaaaaaaaa";
			
			mockMvc.perform(
					get(WORLD_CUPS_PATH)
						.queryParam("keyword", keyword)
				)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("[월드컵 키워드 : 최소: 1, 최대 10]"));
		}

	}
}
