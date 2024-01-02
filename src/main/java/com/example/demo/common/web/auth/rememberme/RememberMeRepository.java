package com.example.demo.common.web.auth.rememberme;

public interface RememberMeRepository {

	Long removeRemember(Long memberId);

	Boolean isRemember(Long memberId);

	void save(Long memberId);

	void signOut(String accessToken, Long memberId);

	Boolean containBlacklistedAccessToken(String accessToken);

}
