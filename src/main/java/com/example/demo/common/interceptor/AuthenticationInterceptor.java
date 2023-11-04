package com.example.demo.common.interceptor;

import com.example.demo.common.web.auth.RequestWithBlackListedAccessToken;
import com.example.demo.common.web.auth.CustomAuthentication;
import com.example.demo.common.web.auth.rememberme.RememberMeRepository;
import com.example.demo.common.jwt.JwtService;
import com.example.demo.common.web.auth.ExpiredAuthenticationException;
import com.example.demo.common.web.memberresolver.AuthenticationUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final String HEADER_TOKEN_NAME = "access-token";
    private static final String PRE_FLIGHT_HTTP_METHOD = "OPTIONS";
    private final RememberMeRepository rememberMeRepository;
    private final JwtService jwtService;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        if(request.getMethod().equals(PRE_FLIGHT_HTTP_METHOD)) {
            return true;
        }
        if (!isRequiredAuthenticationApi(handler, request)) {
            return true;
        }
        String accessToken = request.getHeader(HEADER_TOKEN_NAME);
        Long memberId = jwtService.getPayLoadByToken(accessToken);

        Boolean isBlackListedAccessToken = rememberMeRepository.containBlacklistedAccessToken(accessToken);
        if(isBlackListedAccessToken) {
            throw new RequestWithBlackListedAccessToken(accessToken);
        }
        boolean isRemember = rememberMeRepository.isRemember(memberId);
        if(!isRemember) {
            throw new ExpiredAuthenticationException();
        }
        return true;
    }

    private boolean isRequiredAuthenticationApi(Object handler, HttpServletRequest request) {
        CustomAuthentication customAuthentication = ((HandlerMethod) handler).getMethodAnnotation(CustomAuthentication.class);
        return customAuthentication != null && request.getHeader(HEADER_TOKEN_NAME) != null;
    }

}
