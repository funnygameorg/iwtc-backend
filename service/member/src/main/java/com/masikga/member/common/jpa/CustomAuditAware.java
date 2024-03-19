package com.masikga.member.common.jpa;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

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
