package com.masikga.itwc.domain.member.controller.request;

import com.masikga.itwc.common.web.memberresolver.MemberDto;

public record GetMySummaryResponse(
	Long id,
	String serviceId,
	String nickname
) {
	public GetMySummaryResponse(MemberDto memberDto) {
		this(
			memberDto.getId(),
			memberDto.getServiceId(),
			memberDto.getNickname()
		);
	}
}
