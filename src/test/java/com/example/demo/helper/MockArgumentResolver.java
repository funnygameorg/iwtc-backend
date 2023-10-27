package com.example.demo.helper;

import com.example.demo.common.web.memberresolver.MemberArgumentResolver;
import com.example.demo.common.web.memberresolver.MemberDto;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MockArgumentResolver extends MemberArgumentResolver {
    public MockArgumentResolver() {
        super(null, null);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        return getStubMemberDto();
    }
    
    private MemberDto getStubMemberDto() {
        return new MemberDto(
                1L,
                "TEST_SERVICE_ID",
                "TEST_NICKNAME",
                "TEST_PASSWORD"
        );
    }
}
