package com.example.demo.common.web.memberresolver;

import com.example.demo.common.web.validation.NoSpace;
import com.example.demo.domain.member.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;

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
