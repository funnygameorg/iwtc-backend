package com.example.demo.helper.web;

import com.example.demo.common.web.memberresolver.AuthenticationUtil;
import com.example.demo.common.web.memberresolver.RequiredMemberArgumentResolver;
import com.example.demo.common.web.memberresolver.MemberDto;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MockArgumentResolverRequired extends RequiredMemberArgumentResolver {
    public MockArgumentResolverRequired(AuthenticationUtil authenticationUtil) {
        super(
                null,
                null,
                authenticationUtil
        );
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
