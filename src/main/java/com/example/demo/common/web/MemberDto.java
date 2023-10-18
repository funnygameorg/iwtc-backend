package com.example.demo.common.web;

import com.example.demo.member.model.Member;
import lombok.Builder;

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
