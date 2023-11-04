package com.example.demo.worldcup.controller;

import com.example.demo.helper.web.config.TestWebConfig;
import com.example.demo.common.config.WebConfig;
import com.example.demo.domain.worldcup.controller.WorldCupGameController;
import com.example.demo.domain.worldcup.model.vo.WorldCupDateRange;
import com.example.demo.domain.worldcup.service.WorldCupGameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WorldCupGameController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class)
        })
@Import(TestWebConfig.class)
public class WorldCupGameControllerTest {

    @Autowired private ObjectMapper objectMapper;
    @Autowired private MockMvc mockMvc;

    @MockBean private WorldCupGameService worldCupGameService;

    private final String ROOT_PATH = "/api";

    private final String GET_PAGING_WORLD_CUP_GAME_API = ROOT_PATH + "/world-cups";

    @Test
    @DisplayName("월드컵 리스트 조회 요청 검증")
    public void GET_PAGING_WORLD_CUP_GAME_API_1() throws Exception {
        WorldCupDateRange dateRange = WorldCupDateRange.MONTH;
        mockMvc.perform(
                get(GET_PAGING_WORLD_CUP_GAME_API)
                        .queryParam("dateRange", dateRange.name())
        )
                .andExpect(status().isOk());
        assert true;
    }

    @ParameterizedTest
    @CsvSource(value = {
            "AAAAA",
            "A ",
            "123",
            "Θ\nΘ\n"}
    )
    @DisplayName("월드컵 리스트 조회 요청 검증 - dateRange [Date 포맷이 아닌 요청 데이터](예외)")
    public void GET_PAGING_WORLD_CUP_GAME_API_2(String dateRange) throws Exception {
        mockMvc.perform(
                        get(GET_PAGING_WORLD_CUP_GAME_API)
                                .queryParam("dateRange", dateRange)
                )
                .andExpect(status().is5xxServerError());
        assert true;
    }

    @ParameterizedTest
    @CsvSource(value = {
            "aaaaaaaaaaa"}
    )
    @DisplayName("월드컵 리스트 조회 요청 검증 - keyword [길이 11개 초과 불가] (예외)")
    public void GET_PAGING_WORLD_CUP_GAME_API_3(String keyword) throws Exception {
        mockMvc.perform(
                        get(GET_PAGING_WORLD_CUP_GAME_API)
                                .queryParam("keyword", keyword)
                )
                .andExpect(status().isBadRequest());
        assert true;
    }
}
