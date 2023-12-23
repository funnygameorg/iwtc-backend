package com.example.demo.common.log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.abbreviate;

// 한 번 실행을 보장하기 위해 OncePerRequestFilter 사용
@Slf4j
@Component
@RequiredArgsConstructor
public class WebLoggingFilter extends OncePerRequestFilter {

    private final LogComponent logComponent;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {


        UUID uuid = UUID.randomUUID();
        MDC.put("request_id", uuid.toString());


        // 톰캣에서 한 번만 읽을 수 있게 함. 다시 읽을 수 있는 형태로 래핑
        ContentCachingRequestWrapper wrappingRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappingResponse = new ContentCachingResponseWrapper(response);


        // 로직 수행
        filterChain.doFilter(
                wrappingRequest,
                wrappingResponse
        );


        String uri = wrappingRequest.getRequestURI();


        if(uri.equals("/actuator/prometheus"))
            return;

        String httpMethod = wrappingRequest.getMethod();

        String requestQueryString = wrappingRequest.getQueryString();
        String requestContents = new String(wrappingRequest.getContentAsByteArray());
        String responseContents = new String(wrappingResponse.getContentAsByteArray());

        int httpStatus = wrappingResponse.getStatus();


        wrappingResponse.copyBodyToResponse();


        log.info("{\"is_req\": \"req\", \"method\": \"{}\", \"path\": \"{}\", \"qs\": \"{}\", \"body\": \"{}\" }",
                httpMethod,
                uri,
                requestQueryString,
                logComponent.reduceLongString(requestContents)
        );

        log.info("{ \"is_req\": \"res\", \"method\": \"{}\", \"path\": \"{}\", \"is_success\": \"{}\", \"status\" : \"{}\", \"body\" : \"{}\" }",
                httpMethod,
                uri,
                httpStatus - 200 < 200 ? "SUCCESS" : "FALSE",
                httpStatus,
                logComponent.reduceLongString(responseContents)
        );


        MDC.clear();
    }




}