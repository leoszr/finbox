package leoszr.finance_app.category.controller;

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
class CategoryControllerIT {
	@LocalServerPort int port;
	RestClient client(){ return RestClient.builder().baseUrl("http://localhost:" + port).build(); }
	@Test void crudCategoryForAuthenticatedUser() {
		String auth = "Bearer catuid|cat@example.com|Cat";
		String created = client().post().uri("/categories").header(HttpHeaders.AUTHORIZATION, auth).contentType(MediaType.APPLICATION_JSON).body("{\"name\":\"Mercado\",\"color\":\"#111\",\"type\":\"EXPENSE\"}").retrieve().body(String.class);
		assertThat(created).contains("\"name\":\"Mercado\"").contains("\"specialType\":\"CUSTOM\"");
		String id = created.substring(created.indexOf("\"id\":\"") + 6); id = id.substring(0, id.indexOf('"'));
		String list = client().get().uri("/categories").header(HttpHeaders.AUTHORIZATION, auth).retrieve().body(String.class);
		assertThat(list).contains("Mercado").contains("Não categorizado").contains("Guardado");
		String updated = client().patch().uri("/categories/" + id).header(HttpHeaders.AUTHORIZATION, auth).contentType(MediaType.APPLICATION_JSON).body("{\"name\":\"Supermercado\",\"color\":\"#222\",\"type\":\"EXPENSE\"}").retrieve().body(String.class);
		assertThat(updated).contains("Supermercado");
		client().delete().uri("/categories/" + id).header(HttpHeaders.AUTHORIZATION, auth).retrieve().toBodilessEntity();
	}
}
