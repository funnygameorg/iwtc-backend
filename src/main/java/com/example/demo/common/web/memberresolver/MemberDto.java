package com.example.demo.common.web.memberresolver;

import com.example.demo.member.model.Member;

public record MemberDto (
        Long id,
        String serviceId,
        String nickname,
        String password,
        String accessToken
) {
    public static MemberDto fromEntity(Member member, String accessToken) {
        return new MemberDto(
                member.getId(),
                member.getServiceId(),
                member.getNickname(),
                member.getPassword(),
                accessToken
        );
    }
}
