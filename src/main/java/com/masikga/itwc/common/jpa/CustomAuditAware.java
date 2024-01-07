package com.masikga.itwc.common.jpa;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomAuditAware implements AuditorAware<Long> {

	@NotNull
	@Override
	public Optional<Long> getCurrentAuditor() {
		HttpServletRequest request =
			((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		Long memberId = ((Number)request).longValue();
		return Optional.of(memberId);
	}
}
