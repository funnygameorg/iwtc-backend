package com.example.demo.common.web.memberresolver;

import com.example.demo.common.jpa.NotFoundDataInRequestException;
import com.example.demo.common.jwt.JwtService;
import com.example.demo.common.web.auth.CustomAuthentication;
import com.example.demo.domain.member.exception.NotFoundMemberException;
import com.example.demo.domain.member.model.Member;
import com.example.demo.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;


/**
 * 사용자의 인증값을 필수로 사용하는 경우
 */
@Component
@RequiredArgsConstructor
public class RequiredMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String HEADER_TOKEN_NAME = "access-token";
    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final AuthenticationUtil argumentResolverUtil;

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
        if(optionalToken == null) {
            throw new NotFoundDataInRequestException();
        }


        Long memberId = jwtService.getPayLoadByToken(optionalToken);

        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if(optionalMember.isEmpty()) {
            throw new NotFoundMemberException();
        }

        MemberDto memberDto = MemberDto.fromEntity(optionalMember.get());
        argumentResolverUtil.setMemberIdCurrentRequest(memberDto);
        return Optional.of(memberDto);
    }
}
