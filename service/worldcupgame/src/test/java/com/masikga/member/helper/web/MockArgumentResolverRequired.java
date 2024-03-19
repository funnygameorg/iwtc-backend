package com.masikga.member.helper.web;

import com.masikga.worldcupgame.common.web.memberresolver.MemberDto;
import com.masikga.worldcupgame.common.web.memberresolver.RequiredMemberArgumentResolver;
import com.masikga.worldcupgame.common.web.memberresolver.WebUtil;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

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
