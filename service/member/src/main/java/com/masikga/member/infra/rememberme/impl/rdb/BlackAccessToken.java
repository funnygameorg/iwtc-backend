package com.masikga.member.infra.rememberme.impl.rdb;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class BlackAccessToken {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	private String accessToken;

	private LocalDateTime registeredAt;
}
