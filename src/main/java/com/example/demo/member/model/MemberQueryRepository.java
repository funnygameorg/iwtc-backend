package com.example.demo.member.model;

import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface MemberQueryRepository {

    Boolean existsNickname(String nickname);
    Boolean existsServiceId(String serviceId);
    Optional<Long> findByMemberIdByServiceIdAndPassword(String serviceId, String password);
}
