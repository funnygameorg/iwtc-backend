package com.example.demo.helper.testbase;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;

/**
 * issue : Could not find a valid Docker environment. Please see logs and check configuration
 * https://java.testcontainers.org/supported_docker_environment/
 *
 *
 */
public abstract class ContainerBaseTest {
	static final String REDIS_IMAGE = "redis:6-alpine";
	static final GenericContainer REDIS_CONTAINER;

	static {
		REDIS_CONTAINER = new GenericContainer(REDIS_IMAGE)
			.withExposedPorts(6379)
			.withReuse(true);
		REDIS_CONTAINER.start();
	}

	@DynamicPropertySource
	public static void overrideProps(DynamicPropertyRegistry registry) {
		registry.add("spring.redis.host", REDIS_CONTAINER::getHost);
		registry.add("spring.redis.port", () -> "" + REDIS_CONTAINER.getMappedPort(6379));
	}
}
