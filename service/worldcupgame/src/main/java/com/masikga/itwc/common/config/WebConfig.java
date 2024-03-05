package com.masikga.itwc.common.config;

import com.masikga.itwc.common.config.property.WebProperty;
import com.masikga.itwc.common.jwt.JwtService;
import com.masikga.itwc.common.web.AuthenticationInterceptor;
import com.masikga.itwc.common.web.memberresolver.OptionalMemberArgumentResolver;
import com.masikga.itwc.common.web.memberresolver.RequiredMemberArgumentResolver;
import com.masikga.itwc.common.web.memberresolver.WebUtil;
import com.masikga.itwc.domain.member.repository.MemberRepository;
import com.masikga.itwc.infra.rememberme.RememberMeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final RememberMeRepository rememberMeRepository;
    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final WebProperty webProperty;

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

    // TODO : NEXT.JS SSR 테스트로 모든 오리진에 대하여 허용합니다.
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //.allowedOrigins(webProperty.front()) // 요청에 'Access-Control-Allow-Origin' 추가하기
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("access-token", "refresh-token")
                .allowCredentials(true)
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
