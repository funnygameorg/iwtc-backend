package com.masikga.feign

import com.masikga.model.common.RestApiResponse
import com.masikga.model.member.GetMemberResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "memberClient", url = "\${external.member.host}", configuration = [CustomErrorDecoder::class])
interface MemberClient {

    @GetMapping("/api/members/{memberId}")
    fun findMember(@PathVariable memberId: Long): RestApiResponse<GetMemberResponse>

}