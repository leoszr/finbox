package leoszr.finance_app.budget.controller;

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
class BudgetControllerIT {
	@LocalServerPort int port;
	RestClient client(){ return RestClient.builder().baseUrl("http://localhost:" + port).build(); }
	@Test void crudBudgetForAuthenticatedUser(){
		String auth="Bearer budgetuid|budget@example.com|Budget";
		String created=client().post().uri("/budget").header(HttpHeaders.AUTHORIZATION, auth).contentType(MediaType.APPLICATION_JSON).body("{\"totalAmount\":\"100.00\",\"cycleType\":\"MONTHLY\",\"cycleStartDate\":\"2026-05-01\",\"categories\":[]}").retrieve().body(String.class);
		assertThat(created).contains("MONTHLY").contains("100.00");
		String read=client().get().uri("/budget").header(HttpHeaders.AUTHORIZATION, auth).retrieve().body(String.class);
		assertThat(read).contains("MONTHLY");
		String updated=client().patch().uri("/budget").header(HttpHeaders.AUTHORIZATION, auth).contentType(MediaType.APPLICATION_JSON).body("{\"totalAmount\":\"200.00\",\"cycleType\":\"WEEKLY\",\"cycleStartDate\":\"2026-05-04\",\"categories\":[]}").retrieve().body(String.class);
		assertThat(updated).contains("WEEKLY").contains("200.00");
		String cycle=client().get().uri("/budget/current-cycle?referenceDate=2026-05-07").header(HttpHeaders.AUTHORIZATION, auth).retrieve().body(String.class);
		assertThat(cycle).contains("NORMAL").contains("2026-05-04").contains("2026-05-10");
	}
}
