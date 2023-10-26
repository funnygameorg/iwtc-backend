package com.example.demo.worldcup.controller;

import com.example.demo.common.jwt.JwtService;
import com.example.demo.common.web.auth.rememberme.RememberMeRepository;
import com.example.demo.domain.member.model.repository.MemberRepository;
import com.example.demo.domain.worldcup.controller.WorldCupGameController;
import com.example.demo.domain.worldcup.model.entity.vo.WorldCupDateRange;
import com.example.demo.domain.worldcup.service.WorldCupGameService;
import com.example.demo.common.config.WebConfig;
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

@WebMvcTest(controllers = WorldCupGameController.class)
public class WorldCupGameControllerTest {

    @Autowired private ObjectMapper objectMapper;
    @Autowired private MockMvc mockMvc;

    @MockBean private MemberRepository memberRepository;

    @MockBean private RememberMeRepository rememberMeRepository;

    @MockBean private JwtService jwtService;

    @MockBean private WorldCupGameService worldCupGameService;

    private final String ROOT_PATH = "/api";

    private final String GET_PAGING_WORLD_CUP_GAME_API = ROOT_PATH + "/world-cups";

    @Test
    @DisplayName("월드컵 리스트 조회 요청 검증 성공")
    public void 월드컵_리스트_조회_요청_검증() throws Exception {
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
    @DisplayName("월드컵 리스트 조회 요청 실패 - dateRange")
    public void 월드컵_리스트_조회_요청_검증_실패_dateRange(String dateRange) throws Exception {
        mockMvc.perform(
                        get(GET_PAGING_WORLD_CUP_GAME_API)
                                .queryParam("dateRange", dateRange)
                )
                .andExpect(status().is5xxServerError());
        assert true;
    }

    @ParameterizedTest
    @CsvSource(value = {
            "A",
            "1 ",
            "aaaaaaaaaaa"}
    )
    @DisplayName("월드컵 리스트 조회 요청 실패 - keyword")
    public void 월드컵_리스트_조회_요청_검증_실패_keyword(String keyword) throws Exception {
        mockMvc.perform(
                        get(GET_PAGING_WORLD_CUP_GAME_API)
                                .queryParam("keyword", keyword)
                )
                .andExpect(status().isBadRequest());
        assert true;
    }
}
