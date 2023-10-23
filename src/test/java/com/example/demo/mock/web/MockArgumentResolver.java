package com.example.demo.mock.web;

import com.example.demo.common.web.memberresolver.MemberDto;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MockArgumentResolver implements HandlerMethodArgumentResolver {
    private boolean isSupportsParameter = false;

    public MockArgumentResolver() {}

    public void applyMemberDto() {
        this.isSupportsParameter = true;
    }
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return isSupportsParameter;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        return new MemberDto(
                1L,
                "TEST_SERVICE_ID",
                "TEST_NICKNAME",
                "TEST_PASSWORD",
                "TEST_ACCESS_TOKEN"
        );
    }
}
