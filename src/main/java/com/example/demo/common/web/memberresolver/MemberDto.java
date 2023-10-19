package com.example.demo.common.web.memberresolver;

import com.example.demo.member.model.Member;

public record MemberDto (
        Long id,
        String serviceId,
        String nickname,
        String password
) {
    public static MemberDto fromEntity(Member member) {
        return new MemberDto(
                member.getId(),
                member.getServiceId(),
                member.getNickname(),
                member.getPassword()
        );
    }
}
