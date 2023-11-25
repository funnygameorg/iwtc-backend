package com.example.demo.common.web.log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.abbreviate;

// 한 번 실행을 보장하기 위해 OncePerRequestFilter 사용
@Slf4j
@Component
public class WebLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {


        UUID uuid = UUID.randomUUID();
        MDC.put("request_id", uuid.toString());


        // 톰캣에서 한 번만 읽을 수 있게 함. 다시 읽을 수 있는 형태로 래핑
        ContentCachingRequestWrapper wrappingRequest = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper wrappingResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);


        // 로직 수행
        filterChain.doFilter(
                wrappingRequest,
                wrappingResponse
        );


        String uri = wrappingRequest.getRequestURI();
        String requestContent = new String(wrappingRequest.getContentAsByteArray());
        String resContent = new String(wrappingRequest.getContentAsByteArray());
        int httpStatus = wrappingResponse.getStatus();


        wrappingResponse.copyBodyToResponse();


        // TODO : requestContents 밸류마다 길이 체크하는 것으로 수정하기
        log.info("request url : {}, request body : {}", uri, abbreviate(requestContent,60));
        log.info("response status : {}, response body: {}", httpStatus, resContent );


        MDC.clear();
    }


}