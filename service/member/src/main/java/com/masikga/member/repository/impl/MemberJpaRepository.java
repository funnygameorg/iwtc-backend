package com.masikga.member.repository.impl;

import com.masikga.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByServiceId(String serviceId);

}
