package com.masikga.worldcupgame.common.config.condition;

import com.google.common.base.Objects;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class ProdConfigCondition implements Condition {

	/**
	 `Prod Profile`에서 활성화된다.
	 key 값 spring.profile.active
	 로컬 환경 - spring.profile.active = local
	 테스트 환경 - spring.profile.active = null
	 상용 환경 - spring.profile.active = prod
	 **/
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

		String env = context.getEnvironment().getProperty("spring.profiles.active");
		return Objects.equal(env, "prod");

	}

}
