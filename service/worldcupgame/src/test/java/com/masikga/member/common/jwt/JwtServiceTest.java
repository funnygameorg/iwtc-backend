package com.masikga.member.common.jwt;

import com.masikga.member.common.config.JwtService;
import com.masikga.member.helper.testbase.IntegrationBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class JwtServiceTest implements IntegrationBaseTest {

    @Autowired
    JwtService jwtService;

    @Test
    @DisplayName("getPayLoadByTokenTest : 정상적인 토큰을 넣으면 예외가 일어날 수 없다.")
    public void getPayLoadByTokenTest() {

        // given
        var accessToken = jwtService.createAccessTokenById(1L);

        // when then
        Assertions.assertDoesNotThrow(() -> jwtService.getPayLoadByToken(accessToken));
    }

    @Test
    @DisplayName("getPayLoadByTokenTest2 : 정상적인 페이로드가 나온다.")
    public void getPayLoadByTokenTest2() {

        // given
        var accessToken = jwtService.createAccessTokenById(1L);

        // when then
        var payload = jwtService.getPayLoadByToken(accessToken);

        assertThat(payload).isEqualTo(1L);
    }
}
