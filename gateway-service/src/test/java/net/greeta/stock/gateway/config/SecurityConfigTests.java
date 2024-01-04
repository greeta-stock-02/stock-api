package net.greeta.stock.gateway.config;

import net.greeta.stock.gateway.security.JwtAuthConverter;
import net.greeta.stock.gateway.security.JwtAuthConverterProperties;
import net.greeta.stock.gateway.security.WebSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@WebFluxTest
@Import({WebSecurityConfig.class, JwtAuthConverter.class, JwtAuthConverterProperties.class})
class SecurityConfigTests {

	@Autowired
	WebTestClient webClient;

	@MockBean
	ReactiveClientRegistrationRepository clientRegistrationRepository;

	@Test
	void whenLogoutNotAuthenticatedAndNoCsrfTokenThen403() {
		webClient
				.post()
				.uri("/logout")
				.exchange()
				.expectStatus().isForbidden();
	}

	@Test
	void whenLogoutAuthenticatedAndNoCsrfTokenThen403() {
		webClient
				.mutateWith(SecurityMockServerConfigurers.mockOidcLogin())
				.post()
				.uri("/logout")
				.exchange()
				.expectStatus().isForbidden();
	}

	@Test
	void whenLogoutAuthenticatedAndWithCsrfTokenThen302() {
		when(clientRegistrationRepository.findByRegistrationId("test"))
				.thenReturn(Mono.just(testClientRegistration()));

		webClient
				.mutateWith(SecurityMockServerConfigurers.mockOidcLogin())
				.mutateWith(SecurityMockServerConfigurers.csrf())
				.post()
				.uri("/logout")
				.exchange()
				.expectStatus().isFound();
	}

	private ClientRegistration testClientRegistration() {
		return ClientRegistration.withRegistrationId("test")
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.clientId("test")
				.authorizationUri("https://sso.book-realm.com/auth")
				.tokenUri("https://sso.book-realm.com/token")
				.redirectUri("https://book-realm.com")
				.build();
	}

}
