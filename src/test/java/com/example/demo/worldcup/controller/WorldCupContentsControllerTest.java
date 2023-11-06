package com.example.demo.worldcup.controller;

import com.example.demo.common.config.WebConfig;
import com.example.demo.domain.worldcup.controller.WorldCupContentsController;
import com.example.demo.domain.worldcup.controller.request.ClearWorldCupGameRequest;
import com.example.demo.domain.worldcup.service.WorldCupGameContentsService;
import com.example.demo.helper.config.TestWebConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @Autowired
    private ObjectMapper objectMapper;

    private final static String ROOT_API = "/api/world-cups";
    private final static String GET_AVAILABLE_ROUNDS_API = ROOT_API + "/{worldCupId}/available-rounds";
    private final static String CLEAR_WORLD_CUP_GAME_API = ROOT_API + "/{worldCupId}/clear";

    @Test
    @DisplayName("플레이 가능한 월드컵 게임 라운드 수 조회")
    public void GET_AVAILABLE_ROUNDS_API_1() throws Exception {
        mockMvc.perform(
                get(GET_AVAILABLE_ROUNDS_API, 1L)
        ).andExpect(
                status().isOk()
        );
    }

    @Test
    @DisplayName("이상형 월드컵 게임 종료")
    public void CLEAR_WORLD_CUP_GAME_API_1() throws Exception {
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
