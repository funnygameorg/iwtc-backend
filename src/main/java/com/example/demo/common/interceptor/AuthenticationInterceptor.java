package com.example.demo.common.interceptor;

import com.example.demo.common.web.auth.RequestWithBlackListedAccessToken;
import com.example.demo.common.web.auth.RequireAuth;
import com.example.demo.common.web.auth.rememberme.RememberMeRepository;
import com.example.demo.common.jwt.JwtService;
import com.example.demo.common.web.auth.ExpiredAuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
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
        if(request.getMethod().equals("OPTIONS")) {
            return true;
        }
        if (!isRequiredAuthenticationApi(handler)) {
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

    private boolean isRequiredAuthenticationApi(Object handler) {
        return ((HandlerMethod) handler).getMethodAnnotation(RequireAuth.class) != null;
    }

}
