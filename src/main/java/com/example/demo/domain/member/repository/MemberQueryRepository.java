package com.example.demo.domain.member.repository;

import com.example.demo.domain.member.model.Member;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface MemberQueryRepository {

    Boolean existsNickname(String nickname);
    Boolean existsServiceId(String serviceId);
}
