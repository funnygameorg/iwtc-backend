package com.example.demo.member.model;

import org.springframework.stereotype.Repository;

@Repository
public interface MemberQueryRepository {

    Boolean existsNickname(String nickname);
    Boolean existsServiceId(String serviceId);
    Boolean existsMemberWithServiceIdAndPassword(String serviceId, String password);
}
