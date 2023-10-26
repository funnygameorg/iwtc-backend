package com.example.demo.common.web.memberresolver;

import com.example.demo.common.jwt.JwtService;
import com.example.demo.domain.member.model.Member;
import com.example.demo.domain.member.model.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String HEADER_TOKEN_NAME = "access-token";
    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    private final MemberDto memberDto;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(MemberDto.class);
    }

    @Override
    @Transactional
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Optional.ofNullable(webRequest.getHeader(HEADER_TOKEN_NAME))
                .ifPresent(this::holdMemberDto);
        return memberDto;
    }
    private void holdMemberDto(String accessToken) {
        Long memberId = jwtService.getPayLoadByToken(accessToken);
        Member member = memberRepository.findById(memberId).get();
        memberDto.setAllField(member, accessToken);
    }
}
