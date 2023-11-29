package com.example.demo.helper.testbase;

import com.example.demo.common.config.WebConfig;
import com.example.demo.domain.etc.controller.ETCController;
import com.example.demo.domain.etc.service.CommentService;
import com.example.demo.domain.etc.service.MediaFileService;
import com.example.demo.domain.member.controller.MemberController;
import com.example.demo.domain.member.service.MemberService;
import com.example.demo.domain.worldcup.controller.WorldCupBasedOnAuthController;
import com.example.demo.domain.worldcup.controller.WorldCupContentsController;
import com.example.demo.domain.worldcup.controller.WorldCupGameController;
import com.example.demo.domain.worldcup.service.WorldCupBasedOnAuthService;
import com.example.demo.domain.worldcup.service.WorldCupGameContentsService;
import com.example.demo.domain.worldcup.service.WorldCupGameService;
import com.example.demo.helper.config.TestWebConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.context.annotation.FilterType.*;

@WebMvcTest(
        controllers = {
                WorldCupBasedOnAuthController.class,
                WorldCupContentsController.class,
                WorldCupGameController.class,
                MemberController.class,
                ETCController.class,
        },
        excludeFilters = {
                @Filter(type = ASSIGNABLE_TYPE, classes = WebConfig.class)
        }
)
@Import(TestWebConfig.class)
public abstract class WebMvcBaseTest {

        @Autowired
        protected MockMvc mockMvc;
        @Autowired
        protected ObjectMapper objectMapper;



        @MockBean
        protected CommentService commentService;
        @MockBean
        protected MediaFileService mediaFileService;
        @MockBean
        protected MemberService memberService;
        @MockBean
        private WorldCupGameService worldCupGameService;
        @MockBean
        private WorldCupGameContentsService worldCupGameContentsService;
        @MockBean
        private WorldCupBasedOnAuthService worldCupBasedOnAuthService;



        protected static final String ROOT_PATH = "/api";
        protected static final String WORLD_CUPS_PATH = ROOT_PATH + "/world-cups";
        protected static final String MEMBER_PATH = ROOT_PATH + "/members";

}
