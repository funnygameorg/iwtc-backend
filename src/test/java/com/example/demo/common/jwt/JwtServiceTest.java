package com.example.demo.common.jwt;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.helper.testbase.IntegrationBaseTest;

public class JwtServiceTest implements IntegrationBaseTest {

	@Autowired
	JwtService jwtService;

	@Test
	@DisplayName("getPayLoadByTokenTest : 정상적인 토큰을 넣으면 예외가 일어날 수 없다.")
	public void getPayLoadByTokenTest() {

		// given
		var accessToken = jwtService.createAccessTokenById(1L);

		// when then
		assertDoesNotThrow(() -> jwtService.getPayLoadByToken(accessToken));
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
