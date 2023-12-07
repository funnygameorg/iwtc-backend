package com.example.demo.domain.member.repository.impl;

import com.example.demo.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByServiceId(String serviceId);

}
