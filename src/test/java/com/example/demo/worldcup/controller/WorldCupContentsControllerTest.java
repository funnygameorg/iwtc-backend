package com.example.demo.worldcup.controller;

import com.example.demo.common.config.WebConfig;
import com.example.demo.domain.worldcup.controller.WorldCupContentsController;
import com.example.demo.domain.worldcup.controller.request.ClearWorldCupGameRequest;
import com.example.demo.domain.worldcup.service.WorldCupGameContentsService;
import com.example.demo.helper.config.TestWebConfig;
import com.example.demo.helper.testbase.WebMvcBaseTest;
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

import static com.example.demo.helper.TestConstant.SUCCESS_PREFIX;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
