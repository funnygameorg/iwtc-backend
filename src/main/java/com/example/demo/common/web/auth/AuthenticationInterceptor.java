package com.example.demo.common.web.auth;

import com.example.demo.common.jwt.JwtService;
import com.example.demo.common.web.auth.rememberme.RememberMeRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

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
        boolean isResourceHttpRequestHandler = handler instanceof ResourceHttpRequestHandler;
        if(isResourceHttpRequestHandler) {
            return true;
        }
        boolean isRequiredAuthenticationApi = isRequiredAuthenticationApi((HandlerMethod) handler);
        if (!isRequiredAuthenticationApi) {
            return true;
        }

        String accessToken = request.getHeader("access-token");
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

    private boolean isRequiredAuthenticationApi(HandlerMethod handler) {
        return handler.getMethodAnnotation(RequireAuth.class) != null;
    }

}
