package com.masikga.member.common.web.memberresolver;

import com.masikga.jwt.common.config.JwtService;
import com.masikga.member.common.jpa.NotFoundDataInRequestExceptionMember;
import com.masikga.member.exception.NotFoundMemberExceptionMember;
import com.masikga.member.model.Member;
import com.masikga.member.repository.MemberRepository;
import com.masikga.model.member.CustomAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

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
            throw new NotFoundDataInRequestExceptionMember();
        }

        Long memberId = jwtService.getPayLoadByToken(optionalToken);

        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isEmpty()) {
            throw new NotFoundMemberExceptionMember();
        }

        MemberDto memberDto = MemberDto.fromEntity(optionalMember.get());
        argumentResolverUtil.setMemberIdCurrentRequest(memberDto);
        return Optional.of(memberDto);
    }
}
