package com.masikga.worldcupgame.common.web.memberresolver;

import com.masikga.model.member.CustomAuthentication;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class WebUtil {
    private static final String MEMBER_ID_KEY_IN_REQUEST = "memberId";

    // 인증이 Nullable하지 않는 요청인가?
    public boolean isRequiredAuthentication(MethodParameter parameter) {
        return parameter.getParameterAnnotation(CustomAuthentication.class).required();
    }

    // 글로벌 값에 사용자 id 사용
    public void setMemberIdCurrentRequest(MemberDto memberDto) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        request.setAttribute(MEMBER_ID_KEY_IN_REQUEST, memberDto.id);
    }
}
