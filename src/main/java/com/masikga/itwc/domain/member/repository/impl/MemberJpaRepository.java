package com.masikga.itwc.domain.member.repository.impl;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masikga.itwc.domain.member.model.Member;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByServiceId(String serviceId);

}
