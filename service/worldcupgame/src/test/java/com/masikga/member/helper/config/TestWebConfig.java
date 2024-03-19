package com.masikga.member.helper.config;

import com.masikga.member.common.config.JwtService;
import com.masikga.member.common.config.property.WebProperty;
import com.masikga.member.helper.web.MockArgumentResolverRequired;
import com.masikga.member.helper.web.MockAuthenticationInterceptor;
import com.masikga.member.helper.web.MockWebUtil;
import com.masikga.member.infra.rememberme.RememberMeRepository;
import com.masikga.member.repository.MemberRepository;
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
        super(null, null, null, new WebProperty("http://localhost:8080"));
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MockArgumentResolverRequired(new MockWebUtil()));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MockAuthenticationInterceptor());
    }

    // TODO : MockBean 각자 파일에서 필요한만큼만 모킹하기

    @MockBean
    private RememberMeRepository rememberMeRepository;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private MockWebUtil authenticationUtil;

}
