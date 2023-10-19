package com.example.demo.common.web.auth;

import com.example.demo.common.jwt.JwtService;
import com.example.demo.common.web.auth.rememberme.RememberMeRepository;
import com.example.demo.common.web.auth.rememberme.impl.RedisRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final RememberMeRepository rememberMeRepository;
    private final JwtService jwtService;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {

        boolean isRequiredAuthenticationApi = isRequiredAuthenticationApi((HandlerMethod) handler);
        if (!isRequiredAuthenticationApi) {
            return true;
        }

        Long memberId = jwtService.getPayLoadByToken(request.getHeader("access-token"));

        boolean isRemember = rememberMeRepository.isRemember(memberId);
        if(!isRemember) {
            throw new ExpiredAuthenticationException();
        }
        return true;
    }

    private boolean isRequiredAuthenticationApi(HandlerMethod handler) {
        return handler.getMethodAnnotation(RequireAuth.class) != null;
    }

}
