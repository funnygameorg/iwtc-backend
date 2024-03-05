package com.masikga.itwc.helper;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;

@Component
@RequiredArgsConstructor
public class DataBaseCleanUp implements InitializingBean {
	private final EntityManager entityManager;
	private List<String> tableNames;

	@Override
	public void afterPropertiesSet() throws Exception {
		tableNames = entityManager.getMetamodel().getEntities().stream()
			.filter(entityType -> entityType.getJavaType().getAnnotation(Entity.class) != null)
			.map(entityType -> UPPER_CAMEL.to(LOWER_UNDERSCORE, entityType.getName()))
			.map(this::replaceBadEntityName)
			.collect(Collectors.toList());
	}

	@Transactional
	public void truncateAllEntity() {
		entityManager.flush();
		entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
		for (String tableName : tableNames) {
			entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
		}
		entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
	}

	private String replaceBadEntityName(String entityName) {
		if ("r_d_b_remember_me".equals(entityName))
			return "RDBREMEMBER_ME";
		else
			return entityName;
	}

}
