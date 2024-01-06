package com.example.demo.common.log;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// 한 번 실행을 보장하기 위해 OncePerRequestFilter 사용
@Slf4j
@RequiredArgsConstructor
public class WebLoggingFilter extends OncePerRequestFilter {

	private final LogComponent logComponent;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {

		if (logComponent.excludeMediaFileRequest(request.getRequestURI())) {
			filterChain.doFilter(
				request,
				response
			);
			return;
		}

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

		String path = wrappingRequest.getRequestURI();

		if (path.equals("/actuator/prometheus"))
			return;

		String httpMethod = wrappingRequest.getMethod();

		String requestQueryString = wrappingRequest.getQueryString();
		String requestContents = new String(wrappingRequest.getContentAsByteArray());
		String responseContents = new String(wrappingResponse.getContentAsByteArray());

		int httpStatus = wrappingResponse.getStatus();

		wrappingResponse.copyBodyToResponse();

		log.info(
			"{\"is_req\": \"req\", \"method\": \"{}\", \"path\": \"{}\", \"origin_path\": \"{}\", \"qs\": \"{}\", \"body\": \"{}\" }",
			httpMethod,
			path,
			logComponent.replaceNumberToStar(path),
			requestQueryString,
			logComponent.reduceLongString(requestContents)
		);

		log.info(
			"{ \"is_req\": \"res\", \"method\": \"{}\", \"path\": \"{}\", \"origin_path\": \"{}\", \"is_success\": \"{}\", \"status\" : \"{}\", \"body\" : \"{}\" }",
			httpMethod,
			path,
			logComponent.replaceNumberToStar(path),
			httpStatus - 200 < 200 ? "SUCCESS" : "FALSE",
			httpStatus,
			logComponent.reduceLongString(responseContents)
		);

		MDC.clear();
	}

}