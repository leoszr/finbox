package leoszr.finance_app.transaction.controller;

import leoszr.finance_app.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionControllerIT {
	@LocalServerPort int port;
	RestClient client(){ return RestClient.builder().baseUrl("http://localhost:" + port).build(); }
	@Test void crudTransactionForAuthenticatedUser() {
		String auth = "Bearer txuid|tx@example.com|Tx";
		String created = client().post().uri("/transactions").header(HttpHeaders.AUTHORIZATION, auth).contentType(MediaType.APPLICATION_JSON).body("{\"type\":\"EXPENSE\",\"amount\":\"12.34\",\"occurredOn\":\"2026-05-25\",\"description\":\"Padaria\"}").retrieve().body(String.class);
		assertThat(created).contains("\"type\":\"EXPENSE\"").contains("Não categorizado");
		String id = created.substring(created.indexOf("\"id\":\"") + 6); id = id.substring(0, id.indexOf('"'));
		String read = client().get().uri("/transactions/" + id).header(HttpHeaders.AUTHORIZATION, auth).retrieve().body(String.class);
		assertThat(read).contains("Padaria");
		String list = client().get().uri("/transactions?type=EXPENSE&sort=HIGHEST_AMOUNT&page=0&size=20").header(HttpHeaders.AUTHORIZATION, auth).retrieve().body(String.class);
		assertThat(list).contains("Padaria").contains("totalElements");
		String updated = client().patch().uri("/transactions/" + id).header(HttpHeaders.AUTHORIZATION, auth).contentType(MediaType.APPLICATION_JSON).body("{\"type\":\"INCOME\",\"amount\":\"20.00\",\"occurredOn\":\"2026-05-26\",\"description\":\"Salário\"}").retrieve().body(String.class);
		assertThat(updated).contains("INCOME").contains("Salário");
		client().delete().uri("/transactions/" + id).header(HttpHeaders.AUTHORIZATION, auth).retrieve().toBodilessEntity();
	}
}
