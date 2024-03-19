package com.masikga.member.helper.testbase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masikga.member.controller.MemberController;
import com.masikga.member.helper.config.TestWebConfig;
import com.masikga.member.service.MemberService;
import com.masikga.worldcupgame.common.config.WebConfig;
import com.masikga.worldcupgame.common.log.LogComponent;
import com.masikga.worldcupgame.domain.etc.controller.ETCController;
import com.masikga.worldcupgame.domain.etc.service.CommentService;
import com.masikga.worldcupgame.domain.etc.service.MediaFileService;
import com.masikga.worldcupgame.domain.etc.service.TokenService;
import com.masikga.worldcupgame.domain.worldcup.controller.WorldCupBasedOnAuthController;
import com.masikga.worldcupgame.domain.worldcup.controller.WorldCupContentsBasedOnAuthController;
import com.masikga.worldcupgame.domain.worldcup.controller.WorldCupContentsController;
import com.masikga.worldcupgame.domain.worldcup.controller.WorldCupGameController;
import com.masikga.worldcupgame.domain.worldcup.service.WorldCupBasedOnAuthService;
import com.masikga.worldcupgame.domain.worldcup.service.WorldCupGameContentsService;
import com.masikga.worldcupgame.domain.worldcup.service.WorldCupGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

@WebMvcTest(
        controllers = {
                WorldCupContentsBasedOnAuthController.class,
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
    @MockBean
    private TokenService tokenService;
    @MockBean
    private LogComponent logComponent;

    protected static final String ROOT_PATH = "/api";

    protected static final String WORLD_CUPS_PATH = ROOT_PATH + "/world-cups";
    protected static final String MEMBER_PATH = ROOT_PATH + "/members";

}
