package com.example.demo.worldcup.controller;

import com.example.demo.common.config.WebConfig;
import com.example.demo.domain.worldcup.controller.WorldCupContentsController;
import com.example.demo.domain.worldcup.service.WorldCupGameContentsService;
import com.example.demo.helper.web.config.TestWebConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = WorldCupContentsController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class)
        })
@Import(TestWebConfig.class)
public class WorldCupContentsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorldCupGameContentsService worldCupGameContentsService;

    private final static String ROOT_API = "/api/world-cups";
    private final static String GET_AVAILABLE_ROUNDS_API = ROOT_API + "/{worldCupId}/available-rounds";

    @Test
    @DisplayName("플레이 가능한 월드컵 게임 라운드 수 조회 - 성공")
    public void 플레이_가능한_월드컵_게임_라운드_수_조회() throws Exception {
        mockMvc.perform(
                get(GET_AVAILABLE_ROUNDS_API, 1L)
        ).andExpect(
                status().isOk()
        );
    }
}
