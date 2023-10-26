package com.example.demo.common.config;

import com.example.demo.common.MockArgumentResolver;
import com.example.demo.common.MockAuthenticationInterceptor;
import com.example.demo.common.web.memberresolver.MemberDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;



@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MockArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MockAuthenticationInterceptor());
    }

    @Bean
    public MemberDto memberDto() {
        return new MemberDto(
                1L,
                "TEST_SERVICE_ID",
                "TEST_NICKNAME",
                "TEST_PASSWORD",
                "TEST_ACCESS_TOKEN"
        );
    }
}
