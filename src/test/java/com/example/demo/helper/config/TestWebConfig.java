package com.example.demo.helper.config;

import com.example.demo.common.config.WebConfig;
import com.example.demo.common.interceptor.AuthenticationInterceptor;
import com.example.demo.common.web.memberresolver.MemberArgumentResolver;
import com.example.demo.helper.MockArgumentResolver;
import com.example.demo.helper.MockAuthenticationInterceptor;
import com.example.demo.common.web.memberresolver.MemberDto;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


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
