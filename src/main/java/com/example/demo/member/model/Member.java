package com.example.demo.member.model;

import com.example.demo.common.jpa.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return id.intValue();
    }

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
