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
        String accessToken;

        public void setAllField(Member member, String accessToken) {
            this.id = member.getId();
            this.serviceId = member.getServiceId();
            this.nickname = member.getNickname();
            this.password = member.getPassword();
            this.accessToken = accessToken;
        }
}
