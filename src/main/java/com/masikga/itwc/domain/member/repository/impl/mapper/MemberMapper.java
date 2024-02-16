package com.masikga.itwc.domain.member.repository.impl.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    Boolean existsNickname(String nickname);

    Boolean existsServiceId(String serviceId);

}
