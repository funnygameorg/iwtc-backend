package com.example.demo.member.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceId;

    private String nickname;

    private String password;

    private LocalDateTime signUpdate;

    @Builder
    public Member(String serviceId, String nickname, String password, LocalDateTime signUpdate) {
        this.serviceId = serviceId;
        this.nickname = nickname;
        this.password = password;
        this.signUpdate = signUpdate;
    }
    public static Member signUp(String serviceId, String nickname, String password, LocalDateTime signUpDate) {
        Member instance = new Member();
        instance.serviceId = serviceId;
        instance.nickname = nickname;
        instance.password = password;
        instance.signUpdate = signUpDate;

        return instance;
    }
}
