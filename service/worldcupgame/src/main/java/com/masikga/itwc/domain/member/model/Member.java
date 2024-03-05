package com.masikga.itwc.domain.member.model;

import com.google.common.base.Objects;
import com.masikga.itwc.common.jpa.TimeBaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Comment;
import org.springframework.security.crypto.password.PasswordEncoder;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Table(name = "member")
public class Member extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@NotNull
	@Column(length = 20)
	@Comment("사용자 아이디")
	private String serviceId;

	@NotBlank
	@NotNull
	@Column(length = 10)
	@Comment("사용자 별명")
	private String nickname;

	@NotBlank
	@NotNull
	@Column(length = 250)
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
		Member other = (Member)o;

		return Objects.equal(serviceId, other.getServiceId())
			&& Objects.equal(password, other.getPassword());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(serviceId, password);

	}

}
