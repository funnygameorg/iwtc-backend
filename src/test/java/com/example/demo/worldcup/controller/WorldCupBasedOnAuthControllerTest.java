package com.example.demo.worldcup.controller;

import com.example.demo.common.config.WebConfig;
import com.example.demo.domain.worldcup.controller.WorldCupBasedOnAuthController;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class WorldCupBasedOnAuthControllerTest extends WebMvcBaseTest {

    private final static String GET_MY_GAME_CONTENTS_API = WORLD_CUPS_PATH + "/me/{worldCupId}/contents";

    @Test
    @DisplayName("사용자는 월드컵 게임에 등록된 후보리스트를 볼 수 있다.")
    public void getMyGameContents1() throws Exception {

        mockMvc.perform(
                get(GET_MY_GAME_CONTENTS_API, 1))
                .andExpect(status().isOk());
    }
}
