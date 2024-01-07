package com.masikga.itwc.helper.web;

import java.util.Optional;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.masikga.itwc.common.web.memberresolver.MemberDto;
import com.masikga.itwc.common.web.memberresolver.RequiredMemberArgumentResolver;
import com.masikga.itwc.common.web.memberresolver.WebUtil;

public class MockArgumentResolverRequired extends RequiredMemberArgumentResolver {
	public MockArgumentResolverRequired(WebUtil authenticationUtil) {
		super(
			null,
			null,
			authenticationUtil
		);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		return Optional.of(getStubMemberDto());
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
