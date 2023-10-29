package com.example.demo.common.web.memberresolver;

import com.example.demo.common.jpa.NotFoundDataInRequestException;
import com.example.demo.common.jwt.JwtService;
import com.example.demo.domain.member.exception.NotFoundMemberException;
import com.example.demo.domain.member.model.Member;
import com.example.demo.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

import static java.util.Optional.*;


@Component
@RequiredArgsConstructor
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String HEADER_TOKEN_NAME = "access-token";
    private static final String MEMBER_ID_KEY_IN_REQUEST = "memberId";
    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(MemberDto.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        Optional<String> optionalToken = ofNullable(webRequest.getHeader(HEADER_TOKEN_NAME));
        if(optionalToken.isEmpty()) {
            throw new NotFoundDataInRequestException();
        }

        Long memberId = jwtService.getPayLoadByToken(optionalToken.get());

        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if(optionalMember.isEmpty()) {
            throw new NotFoundMemberException();
        }

        MemberDto memberDto = MemberDto.fromEntity(optionalMember.get());
        setMemberIdCurrentRequest(memberDto);
        return memberDto;
    }

    private void setMemberIdCurrentRequest(MemberDto memberDto) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes())
                .getRequest();
        request.setAttribute(MEMBER_ID_KEY_IN_REQUEST, memberDto.id);
    }

}
