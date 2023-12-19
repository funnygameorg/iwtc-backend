package com.example.demo.common.config.condition;

import com.google.common.base.Objects;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class NotProdConfigCondition implements Condition {

    // `Local, Test Profile`에서 활성화된다.
    // 로컬 환경 - spring.profile.active = local
    // 테스트 환경 - spring.profile.active = null
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        String env = context.getEnvironment().getProperty("spring.profiles.active");
        return Objects.equal(env, "local") || Objects.equal(env, null);

    }


}
