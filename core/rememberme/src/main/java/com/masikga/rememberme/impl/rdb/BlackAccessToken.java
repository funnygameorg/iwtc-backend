package com.masikga.rememberme.impl.rdb;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "black_access_token")
@AllArgsConstructor
@NoArgsConstructor
public class BlackAccessToken {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String accessToken;

    private LocalDateTime registeredAt;
}
