package leoszr.finance_app.dashboard.controller;

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
class DashboardControllerIT {
	@LocalServerPort int port;
	RestClient client(){ return RestClient.builder().baseUrl("http://localhost:"+port).build(); }
	@Test void dashboardContainsSections(){
		String auth="Bearer dashuid|dash@example.com|Dash";
		client().post().uri("/budget").header(HttpHeaders.AUTHORIZATION,auth).contentType(MediaType.APPLICATION_JSON).body("{\"totalAmount\":\"500.00\",\"cycleType\":\"MONTHLY\",\"cycleStartDate\":\"2026-05-01\",\"categories\":[]}").retrieve().body(String.class);
		client().post().uri("/transactions").header(HttpHeaders.AUTHORIZATION,auth).contentType(MediaType.APPLICATION_JSON).body("{\"type\":\"INCOME\",\"amount\":\"1000.00\",\"occurredOn\":\"2026-05-02\",\"description\":\"Salário\"}").retrieve().body(String.class);
		String res=client().get().uri("/dashboard?referenceDate=2026-05-10").header(HttpHeaders.AUTHORIZATION,auth).retrieve().body(String.class);
		assertThat(res).contains("summary").contains("budget").contains("latestTransactions").contains("Salário");
	}
}
