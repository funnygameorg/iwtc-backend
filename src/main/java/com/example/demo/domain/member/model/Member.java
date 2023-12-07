package com.example.demo.domain.member.model;

import com.example.demo.common.jpa.TimeBaseEntity;
import com.google.common.base.Objects;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Comment;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.google.common.base.Objects.*;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Table(name = "MEMBER")
public class Member extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NotNull
    @Comment("사용자 아이디")
    private String serviceId;

    @NotBlank
    @NotNull
    @Comment("사용자 별명")
    private String nickname;

    @NotBlank
    @NotNull
    @Comment("사용자 암호")
    private String password;





    public static Member signUp(PasswordEncoder passwordEncoder, String serviceId, String nickname, String password) {

        passwordEncoder.encode(password);

        return new Member(
                null,
                serviceId,
                nickname,
                passwordEncoder.encode(password)
        );
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
