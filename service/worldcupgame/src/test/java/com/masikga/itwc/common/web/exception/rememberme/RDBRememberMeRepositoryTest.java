package com.masikga.itwc.common.web.exception.rememberme;

import com.masikga.itwc.common.jwt.JwtService;
import com.masikga.itwc.helper.DataBaseCleanUp;
import com.masikga.itwc.helper.TestConstant;
import com.masikga.itwc.helper.testbase.IntegrationBaseTest;
import com.masikga.itwc.infra.rememberme.impl.rdb.RDBRememberMe;
import com.masikga.itwc.infra.rememberme.impl.rdb.RDBRememberMeRepository;
import com.masikga.itwc.infra.rememberme.impl.rdb.RememberMeJpaRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class RDBRememberMeRepositoryTest implements IntegrationBaseTest {

	@Autowired
	private RDBRememberMeRepository rdbRememberMeRepository;

	@Autowired
	private RememberMeJpaRepository rememberMeJpaRepository;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private EntityManager em;

	@Autowired
	private DataBaseCleanUp dataBaseCleanUp;

	@AfterEach
	public void tearDown() {
		dataBaseCleanUp.truncateAllEntity();
	}

	@Nested
	@DisplayName("로그인 유지상태를 제거할 수 있다.")
	class removeRemember {

		@Test
		@DisplayName(TestConstant.SUCCESS_PREFIX)
		public void success1() {

			// given
			var remember = RDBRememberMe.builder()
				.memberId(1L)
				.rememberAt(now())
				.build();

			rememberMeJpaRepository.save(remember);

			// when
			var removedMemberId = rdbRememberMeRepository.removeRemember(1L);
			var optionalRemember = rememberMeJpaRepository.findByMemberId(1L);

			// then
			assertAll(
				() -> assertThat(removedMemberId).isEqualTo(1),
				() -> assertThat(optionalRemember.isEmpty()).isTrue()
			);

		}

	}

	@Nested
	@DisplayName("로그인 유지상태인지 확인할 수 있다.")
	class isRemember {

		@Test
		@DisplayName(TestConstant.SUCCESS_PREFIX)
		public void success1() {

			// given
			var remember = RDBRememberMe.builder()
				.memberId(1L)
				.rememberAt(now())
				.build();

			rememberMeJpaRepository.save(remember);

			// when
			var isSignIn = rdbRememberMeRepository.isRemember(1L);

			// then
			assertThat(isSignIn).isTrue();
		}

		@Test
		@DisplayName(TestConstant.SUCCESS_PREFIX + "로그인하지 않은 사용자는 유지상태가 아님")
		public void success2() {
			// when
			var isSignIn = rdbRememberMeRepository.isRemember(1L);

			// then
			assertThat(isSignIn).isFalse();
		}

		@Test
		@DisplayName(TestConstant.SUCCESS_PREFIX + "만료시간과 현재시간이 동일, 토큰 시간 만료")
		public void success3() {
			// 21600초 - 360분 - 6시간
			// given
			var remember = RDBRememberMe.builder()
				.memberId(1L)
				.rememberAt(now().minusHours(6))
				.build();

			rememberMeJpaRepository.save(remember);

			// when
			var isSignIn = rdbRememberMeRepository.isRemember(1L);

			// then
			assertThat(isSignIn).isFalse();
		}

		@Test
		@DisplayName(TestConstant.SUCCESS_PREFIX + "만료시간 1초 차이로 토큰 유효")
		public void success4() {
			// 21600초 - 360분 - 6시간
			// given
			var remember = RDBRememberMe.builder()
				.memberId(1L)
				.rememberAt(now().minusHours(6).plusSeconds(1))
				.build();

			rememberMeJpaRepository.save(remember);

			// when
			var isSignIn = rdbRememberMeRepository.isRemember(1L);

			// then
			assertThat(isSignIn).isTrue();
		}

		@Test
		@DisplayName(TestConstant.SUCCESS_PREFIX + "만료시간 1초 차이로 토큰 만료")
		public void success5() {
			// 21600초 - 360분 - 6시간
			// given
			var remember = RDBRememberMe.builder()
				.memberId(1L)
				.rememberAt(now().minusHours(6).minusSeconds(1))
				.build();

			rememberMeJpaRepository.save(remember);

			// when
			var isSignIn = rdbRememberMeRepository.isRemember(1L);

			// then
			assertThat(isSignIn).isFalse();
		}

	}

	@Nested
	@DisplayName("로그인 유지상태로 만들 수 있다.")
	class save {

		@Test
		@DisplayName(TestConstant.SUCCESS_PREFIX)
		public void success() {

			// when
			rdbRememberMeRepository.save(1L);

			// then
			assertThat(rememberMeJpaRepository.findByMemberId(1L).isPresent()).isTrue();
		}
	}

	@Nested
	@DisplayName("로그아웃을 할 수 있다.")
	class signOut {

		@Test
		@DisplayName(TestConstant.SUCCESS_PREFIX)
		public void success1() {

			// given
			var accessToken = jwtService.createAccessTokenById(1L);
			var rememberMe = RDBRememberMe.builder()
				.memberId(1L)
				.rememberAt(now())
				.build();

			rememberMeJpaRepository.save(rememberMe);

			// when
			rdbRememberMeRepository.signOut(accessToken, 1L);

			// then
			assertThat(rememberMeJpaRepository.findByMemberId(1L).isEmpty()).isTrue();

		}
	}

	@Nested
	@DisplayName("블랙리스트에 포함된 액세스 토큰인지 확인할 수 있다.")
	@Transactional
	class containBlacklistedAccessToken {

		@Test
		@DisplayName(TestConstant.SUCCESS_PREFIX)
		public void success1() {

			// given
			var accessToken = "testToken";

			em.createNativeQuery(
					"INSERT INTO black_access_token(access_token, registered_at) VALUES (:accessToken, :now)")
				.setParameter("accessToken", accessToken)
				.setParameter("now", now())
				.executeUpdate();

			// when & then
			assertThat(rdbRememberMeRepository.containBlacklistedAccessToken("testToken")).isTrue();
		}

		@Test
		@DisplayName(TestConstant.SUCCESS_PREFIX + "블랙 리스트 엑세스 토큰이 존재하지 않는다.")
		public void success2() {

			// when & then
			assertThat(rdbRememberMeRepository.containBlacklistedAccessToken("testToken")).isFalse();
		}

	}

}
