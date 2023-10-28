package com.example.demo.common.web.memberresolver;

import com.example.demo.domain.member.model.Member;
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

    public static MemberDto fromEntity(Member member) {
        return new MemberDto(
                member.getId(),
                member.getServiceId(),
                member.getNickname(),
                member.getPassword()
        );
    }
}
