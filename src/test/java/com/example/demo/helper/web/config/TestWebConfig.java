package com.example.demo.helper.web.config;

import com.example.demo.common.config.WebConfig;
import com.example.demo.common.jwt.JwtService;
import com.example.demo.common.web.auth.rememberme.RememberMeRepository;
import com.example.demo.domain.member.repository.MemberRepository;
import com.example.demo.helper.web.MockArgumentResolver;
import com.example.demo.helper.web.MockAuthenticationInterceptor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.List;


/*
    Controller 테스트에 필요한 환경
 */
@TestConfiguration
public class TestWebConfig extends WebConfig {

    public TestWebConfig() {
        super(null, null);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MockArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MockAuthenticationInterceptor());
    }

    @MockBean
    private RememberMeRepository rememberMeRepository;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private MemberRepository memberRepository;
}
