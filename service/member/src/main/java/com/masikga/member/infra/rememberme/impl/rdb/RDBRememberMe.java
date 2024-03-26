package com.masikga.member.infra.rememberme.impl.rdb;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Builder
@Table(name = "rdbremember_me")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RDBRememberMe {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long memberId;

    private LocalDateTime rememberAt;

    // 로그인 최종 유지시간이 만료되었는가?
    public boolean isExpired(LocalDateTime now, Long refreshTokenValiditySeconds) {

        var expiredTime = rememberAt.plusSeconds(refreshTokenValiditySeconds / 1000);
        return !expiredTime.isAfter(now);

    }

}
