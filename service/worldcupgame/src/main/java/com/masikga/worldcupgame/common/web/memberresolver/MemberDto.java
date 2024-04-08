package com.masikga.worldcupgame.common.web.memberresolver;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberDto {
    Long id;
    String serviceId;
    String nickname;
    String password;

    public static MemberDto fromEntity(Long memberId, String serviceId, String nickname, String password) {
        return new MemberDto(memberId, serviceId, nickname, password);
    }
}
