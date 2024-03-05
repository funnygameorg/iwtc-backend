package com.masikga.itwc.domain.worldcup.controller;

import com.masikga.itwc.domain.worldcup.controller.request.ClearWorldCupGameRequest;
import com.masikga.itwc.helper.testbase.WebMvcBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.masikga.itwc.helper.TestConstant.SUCCESS_PREFIX;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WorldCupContentsControllerTest extends WebMvcBaseTest {
    private final static String GET_AVAILABLE_ROUNDS_API = WORLD_CUPS_PATH + "/{worldCupId}/available-rounds";
    private final static String CLEAR_WORLD_CUP_GAME_API = WORLD_CUPS_PATH + "/{worldCupId}/clear";

    @Nested
    @DisplayName("플레이 가능한 월드컵 게임 라운드 수 조회 요청 검증")
    public class getAvailableRoundsApi {

        @Test
        @DisplayName(SUCCESS_PREFIX)
        public void success() throws Exception {

            mockMvc.perform(
                    get(GET_AVAILABLE_ROUNDS_API, 1L)
            ).andExpect(
                    status().isOk()
            );

        }

    }

    @Nested
    @DisplayName("이상형 월드컵 게임 종료 요청 검증")
    public class clearWorldCupGame {

        @Test
        @DisplayName(SUCCESS_PREFIX)
        public void success() throws Exception {

            var request = ClearWorldCupGameRequest
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

}
