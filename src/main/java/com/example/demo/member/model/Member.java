package com.example.demo.member.model;

import com.example.demo.common.jpa.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceId;

    private String nickname;

    private String password;

    @Builder
    public Member(Long id, String serviceId, String nickname, String password) {
        this.id = id;
        this.serviceId = serviceId;
        this.nickname = nickname;
        this.password = password;
    }

    public static Member signUp(String serviceId, String nickname, String password) {
        return new Member(null, serviceId, nickname, password);
    }
}
