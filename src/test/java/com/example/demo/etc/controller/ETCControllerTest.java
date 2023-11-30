package com.example.demo.etc.controller;

import com.example.demo.domain.etc.controller.request.WriteCommentRequest;
import com.example.demo.domain.member.controller.request.SignInRequest;
import com.example.demo.helper.testbase.WebMvcBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static com.example.demo.helper.TestConstant.EXCEPTION_PREFIX;
import static com.example.demo.helper.TestConstant.SUCCESS_PREFIX;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ETCControllerTest extends WebMvcBaseTest {

    private static final String GET_COMMENTS_LIST_API = ROOT_PATH + "/world-cups/{worldCupId}/comments";
    private static final String WRITE_COMMENT_API = ROOT_PATH + "/world-cups/{worldCupId}/contents/{contentsId}/comments";




    @Test
    @DisplayName(SUCCESS_PREFIX + "월드컵의 댓글 리스트 조회")
    public void getCommentList() throws Exception{

        mockMvc.perform(
                get(GET_COMMENTS_LIST_API, 1L)
                        .param("offset" , "0")
                )
                .andExpect(status().isOk());
    }




    @Test
    @DisplayName(SUCCESS_PREFIX + "월드컵의 댓글 작성")
    public void writeComment() throws Exception{

        var request = WriteCommentRequest.builder()
                .body("ABC")
                .nickname("손님1")
                .build();

        mockMvc.perform(
                        post(WRITE_COMMENT_API, 1L)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }



}
