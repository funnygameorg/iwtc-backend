package com.masikga.itwc.domain.member.repository;

public interface MemberQueryRepository {

    Boolean existsNickname(String nickname);

    Boolean existsServiceId(String serviceId);

    Boolean existsNicknameV2(String nickname);

    Boolean existsServiceIdV2(String serviceId);
}
