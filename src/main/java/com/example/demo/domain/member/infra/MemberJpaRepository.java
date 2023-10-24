package com.example.demo.domain.member.infra;

import com.example.demo.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> { }
