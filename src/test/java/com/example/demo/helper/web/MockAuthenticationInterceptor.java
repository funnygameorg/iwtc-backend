package com.example.demo.helper.web;

import com.example.demo.common.web.AuthenticationInterceptor;
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
