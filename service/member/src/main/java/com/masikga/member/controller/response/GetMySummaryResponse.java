package com.masikga.member.controller.response;

import com.masikga.member.common.web.memberresolver.MemberDto;

public record GetMySummaryResponse(
        Long memberId,
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
