package com.example.demo.worldcup.controller;

import com.example.demo.helper.config.TestWebConfig;
import com.example.demo.common.config.WebConfig;
import com.example.demo.domain.worldcup.controller.WorldCupGameController;
import com.example.demo.domain.worldcup.controller.vo.WorldCupDateRange;
import com.example.demo.domain.worldcup.service.WorldCupGameService;
import com.example.demo.helper.testbase.WebMvcBaseTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

import static com.example.demo.helper.TestConstant.EXCEPTION_PREFIX;
import static com.example.demo.helper.TestConstant.SUCCESS_PREFIX;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
            assert true;
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
            assert true;
        }

        @ParameterizedTest
        @CsvSource(value = {
                "aaaaaaaaaaa"}
        )
        @DisplayName(EXCEPTION_PREFIX + " keyword : 길이 11개 초과 불가")
        public void fail2(String keyword) throws Exception {
            mockMvc.perform(
                            get(WORLD_CUPS_PATH)
                                    .queryParam("keyword", keyword)
                    )
                    .andExpect(status().isBadRequest());
            assert true;
        }

    }
}
