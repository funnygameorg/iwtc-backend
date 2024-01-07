package com.masikga.itwc.infra.rememberme.impl.rdb;

import static jakarta.persistence.GenerationType.*;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

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
