package com.masikga.member.helper.web;

import com.masikga.worldcupgame.common.web.AuthenticationInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MockAuthenticationInterceptor extends AuthenticationInterceptor {
    public MockAuthenticationInterceptor() {
        super(null, null);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return true;
    }
}
