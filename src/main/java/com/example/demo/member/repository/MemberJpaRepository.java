package com.example.demo.member.repository;

import com.example.demo.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> { }
