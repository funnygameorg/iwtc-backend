package com.masikga.itwc.common.web.memberresolver;

import java.util.Optional;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.masikga.itwc.common.jpa.NotFoundDataInRequestException;
import com.masikga.itwc.common.jwt.JwtService;
import com.masikga.itwc.common.web.CustomAuthentication;
import com.masikga.itwc.domain.member.exception.NotFoundMemberException;
import com.masikga.itwc.domain.member.model.Member;
import com.masikga.itwc.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

/**
 * 사용자의 인증값을 필수로 사용하는 경우
 */
@RequiredArgsConstructor
public class RequiredMemberArgumentResolver implements HandlerMethodArgumentResolver {

	private static final String HEADER_TOKEN_NAME = "access-token";
	private final JwtService jwtService;
	private final MemberRepository memberRepository;
	private final WebUtil argumentResolverUtil;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(CustomAuthentication.class)
			&& argumentResolverUtil.isRequiredAuthentication(parameter);
	}

	@Override
	public Object resolveArgument(
		MethodParameter parameter,
		ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest,
		WebDataBinderFactory binderFactory
	) {
		String optionalToken = webRequest.getHeader(HEADER_TOKEN_NAME);
		if (optionalToken == null) {
			throw new NotFoundDataInRequestException();
		}

		Long memberId = jwtService.getPayLoadByToken(optionalToken);

		Optional<Member> optionalMember = memberRepository.findById(memberId);
		if (optionalMember.isEmpty()) {
			throw new NotFoundMemberException();
		}

		MemberDto memberDto = MemberDto.fromEntity(optionalMember.get());
		argumentResolverUtil.setMemberIdCurrentRequest(memberDto);
		return Optional.of(memberDto);
	}
}
