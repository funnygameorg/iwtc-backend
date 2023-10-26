package com.example.demo.common.interceptor;

import com.example.demo.common.jwt.JwtService;
import com.example.demo.common.web.memberresolver.MemberDto;
import com.example.demo.domain.member.model.Member;
import com.example.demo.domain.member.model.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.*;

@RequiredArgsConstructor
public class MemberHolderInterceptor implements HandlerInterceptor {
    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final MemberDto memberDto;
    private static final String HEADER_TOKEN_NAME = "access-token";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Optional.ofNullable(request.getHeader(HEADER_TOKEN_NAME))
                .ifPresent(this::holdMemberDto);
        return true;
    }

    private void holdMemberDto(String accessToken) {
        Long memberId = jwtService.getPayLoadByToken(accessToken);
        Member member = memberRepository.findById(memberId).get();
        memberDto.setAllField(member, accessToken);
    }
}
