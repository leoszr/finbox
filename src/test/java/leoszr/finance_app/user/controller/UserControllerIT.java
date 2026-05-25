package leoszr.finance_app.user.controller;

import leoszr.finance_app.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIT {
	@LocalServerPort int port;
	RestClient client() { return RestClient.builder().baseUrl("http://localhost:" + port).build(); }
	@Test void anonymousCannotAccessMe() { assertThatThrownBy(() -> client().get().uri("/me").retrieve().toBodilessEntity()).isInstanceOf(RestClientResponseException.class).extracting(e -> ((RestClientResponseException)e).getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED); }
	@Test void getMeReturnsAuthenticatedUser() { String body = client().get().uri("/me").header(HttpHeaders.AUTHORIZATION, "Bearer uid1|u1@example.com|User One").retrieve().body(String.class); assertThat(body).contains("\"externalAuthId\":\"uid1\"").contains("\"defaultPeriod\":\"MONTHLY\""); }
	@Test void patchMeUpdatesProfile() { String body = client().patch().uri("/me").header(HttpHeaders.AUTHORIZATION, "Bearer uid2|u2@example.com|User Two").contentType(MediaType.APPLICATION_JSON).body("{\"name\":\"Novo\",\"theme\":\"DARK\"}").retrieve().body(String.class); assertThat(body).contains("\"name\":\"Novo\"").contains("\"email\":\"u2@example.com\""); }
	@Test void patchPreferencesUpdatesPreferences() { String body = client().patch().uri("/me/preferences").header(HttpHeaders.AUTHORIZATION, "Bearer uid3|u3@example.com|User Three").contentType(MediaType.APPLICATION_JSON).body("{\"defaultPeriod\":\"WEEKLY\",\"defaultSort\":\"OLDEST_FIRST\"}").retrieve().body(String.class); assertThat(body).contains("\"defaultPeriod\":\"WEEKLY\""); }
}
