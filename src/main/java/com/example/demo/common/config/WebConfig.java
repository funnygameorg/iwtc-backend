package com.example.demo.common.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.common.jwt.JwtService;
import com.example.demo.common.web.AuthenticationInterceptor;
import com.example.demo.common.web.memberresolver.OptionalMemberArgumentResolver;
import com.example.demo.common.web.memberresolver.RequiredMemberArgumentResolver;
import com.example.demo.common.web.memberresolver.WebUtil;
import com.example.demo.domain.member.repository.MemberRepository;
import com.example.demo.infra.rememberme.RememberMeRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final RememberMeRepository rememberMeRepository;
	private final MemberRepository memberRepository;
	private final JwtService jwtService;

	@Value("${origin.front}")
	private String frontOrigin;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(requiredMemberArgumentResolver());
		resolvers.add(optionalMemberArgumentResolver());
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry
			.addInterceptor(authenticationInterceptor())
			.addPathPatterns("/api/**")
			.excludePathPatterns("/api/members/sign-out");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("*") // 요청에 'Access-Control-Allow-Origin' 추가하기
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
			.allowedHeaders("*")
			.exposedHeaders("access-token", "refresh-token")
			.maxAge(3000);
	}

	private AuthenticationInterceptor authenticationInterceptor() {
		return new AuthenticationInterceptor(rememberMeRepository, jwtService);
	}

	private RequiredMemberArgumentResolver requiredMemberArgumentResolver() {
		return new RequiredMemberArgumentResolver(jwtService, memberRepository, webUtil());
	}

	private OptionalMemberArgumentResolver optionalMemberArgumentResolver() {
		return new OptionalMemberArgumentResolver(jwtService, memberRepository, webUtil());
	}

	private WebUtil webUtil() {
		return new WebUtil();
	}
}
