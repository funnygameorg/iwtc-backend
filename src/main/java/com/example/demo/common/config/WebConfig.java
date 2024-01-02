package com.example.demo.common.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.common.web.AuthenticationInterceptor;
import com.example.demo.common.web.memberresolver.OptionalMemberArgumentResolver;
import com.example.demo.common.web.memberresolver.RequiredMemberArgumentResolver;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final AuthenticationInterceptor authenticationInterceptor;
	private final RequiredMemberArgumentResolver memberArgumentResolver;
	private final OptionalMemberArgumentResolver optionalMemberArgumentResolver;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(memberArgumentResolver);
		resolvers.add(optionalMemberArgumentResolver);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry
			.addInterceptor(authenticationInterceptor)
			.addPathPatterns("/api/**")
			.excludePathPatterns("/api/members/sign-out");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("*")
			.allowedMethods("*")
			.allowedHeaders("*")
			.exposedHeaders("access-token", "refresh-token")
			.maxAge(3000);
	}

}
