package com.example.demo.infra.rememberme.impl.rdb;

import static jakarta.persistence.GenerationType.*;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
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
