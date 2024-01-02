package com.example.demo.domain.member.repository.impl;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.member.model.Member;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByServiceId(String serviceId);

}
