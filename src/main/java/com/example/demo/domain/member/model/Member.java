package com.example.demo.domain.member.model;

import com.example.demo.common.jpa.TimeBaseEntity;
import com.google.common.base.Objects;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Comment;

import static com.google.common.base.Objects.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("사용자 아이디")
    private String serviceId;

    @Comment("사용자 별명")
    private String nickname;

    @Comment("사용자 암호")
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Member other = (Member) o;

        return Objects.equal(serviceId, other.getServiceId())
                && Objects.equal(password, other.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(serviceId, password);

    }
}
