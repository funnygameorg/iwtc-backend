package com.masikga.member.repository;

import com.masikga.member.repository.impl.MemberJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends MemberJpaRepository, MemberQueryRepository {

}
