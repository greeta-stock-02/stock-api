package net.greeta.stock.gateway;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class GatewayServiceApplicationTests {

	private static final int REDIS_PORT = 6379;

	private static final int KEYCLOAK_PORT = 8080;

	@Container
	static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7.0"))
			.withExposedPorts(REDIS_PORT);

	static final KeycloakContainer keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:22.0.1");

	@MockBean
	ReactiveClientRegistrationRepository clientRegistrationRepository;

	@DynamicPropertySource
	static void redisProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.redis.host", () -> redis.getHost());
		registry.add("spring.redis.port", () -> redis.getMappedPort(REDIS_PORT));

		keycloak.start();
		registry.add("spring.keycloak.server-url", () -> keycloak.getAuthServerUrl());
	}

	@Test
	void verifyThatSpringContextLoads() {
	}

}
