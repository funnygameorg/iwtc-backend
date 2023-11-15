package com.example.demo.worldcup.controller;

import com.example.demo.common.config.WebConfig;
import com.example.demo.domain.worldcup.controller.WorldCupBasedOnAuthController;
import com.example.demo.domain.worldcup.controller.request.CreateWorldCupRequest;
import com.example.demo.domain.worldcup.model.vo.VisibleType;
import com.example.demo.domain.worldcup.service.WorldCupBasedOnAuthService;
import com.example.demo.helper.config.TestWebConfig;
import com.example.demo.helper.testbase.WebMvcBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.example.demo.domain.worldcup.model.vo.VisibleType.PRIVATE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class WorldCupBasedOnAuthControllerTest extends WebMvcBaseTest {

    private final static String GET_MY_GAME_CONTENTS_API = WORLD_CUPS_PATH + "/me/{worldCupId}/contents";
    private final static String UPDATE_MY_WORLD_CUP = WORLD_CUPS_PATH + "/me/{worldCupId}" ;
    private final static String CREATE_MY_WORLD_CUP = WORLD_CUPS_PATH + "/me" ;

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
}
