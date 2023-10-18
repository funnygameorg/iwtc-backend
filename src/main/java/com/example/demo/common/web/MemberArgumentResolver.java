package com.example.demo.common.web;

import com.example.demo.common.jwt.JwtService;
import com.example.demo.member.exception.NotFoundMemberException;
import com.example.demo.member.model.Member;
import com.example.demo.member.model.MemberQueryRepository;
import com.example.demo.member.model.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


@Component
@RequiredArgsConstructor
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberRepository memberRepository;
    private final JwtService jwtService;

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
