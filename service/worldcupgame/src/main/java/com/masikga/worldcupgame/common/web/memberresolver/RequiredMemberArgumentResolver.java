package com.masikga.worldcupgame.common.web.memberresolver;

import com.masikga.feign.FeignException;
import com.masikga.feign.MemberClient;
import com.masikga.jwt.common.config.JwtService;
import com.masikga.model.member.CustomAuthentication;
import com.masikga.worldcupgame.common.jpa.NotFoundDataInRequestExceptionMember;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private MemberClient memberClient;
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

        var memberResponse = memberClient.findMember(memberId);
        if (memberResponse.getCode() != 200) {
            throw new FeignException(memberResponse.getCode(), "존재하지 않는 사용자입니다.");
        }

        var member = memberResponse.getData();
        MemberDto memberDto = MemberDto.fromEntity(
                member.getMemberId(),
                member.getServiceId(),
                member.getNickname(),
                member.getPassword()
        );

        argumentResolverUtil.setMemberIdCurrentRequest(memberDto);
        return Optional.of(memberDto);
    }
}
