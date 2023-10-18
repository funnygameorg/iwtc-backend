package com.example.demo.common.web;

import com.example.demo.common.jwt.JwtService;
import com.example.demo.member.exception.NotFoundMemberException;
import com.example.demo.member.model.Member;
import com.example.demo.member.model.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(MemberDto.class);
    }

    @Override
    @Transactional
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        String accessToken = webRequest.getHeader("access-token");
        Long serviceId = Long.parseLong(jwtService.getPayLoadByToken(accessToken));
        Member member = memberRepository
                .findById(serviceId)
                .orElseThrow(NotFoundMemberException::new);

        return MemberDto.fromEntity(member);
    }
}
