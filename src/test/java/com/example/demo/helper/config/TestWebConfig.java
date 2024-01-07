package com.example.demo.helper.config;

import java.util.List;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.example.demo.common.config.WebConfig;
import com.example.demo.common.config.property.WebProperty;
import com.example.demo.common.jwt.JwtService;
import com.example.demo.domain.member.repository.MemberRepository;
import com.example.demo.helper.web.MockArgumentResolverRequired;
import com.example.demo.helper.web.MockAuthenticationInterceptor;
import com.example.demo.helper.web.MockWebUtil;
import com.example.demo.infra.rememberme.RememberMeRepository;

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
