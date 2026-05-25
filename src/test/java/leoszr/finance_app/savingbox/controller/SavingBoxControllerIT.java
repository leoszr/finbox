package leoszr.finance_app.savingbox.controller;

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
class SavingBoxControllerIT {
	@LocalServerPort int port;
	RestClient client(){ return RestClient.builder().baseUrl("http://localhost:" + port).build(); }
	@Test void crudBoxForAuthenticatedUser(){
		String auth="Bearer boxuid|box@example.com|Box";
		String list=client().get().uri("/boxes").header(HttpHeaders.AUTHORIZATION,auth).retrieve().body(String.class);
		assertThat(list).contains("Economias").contains("BRL");
		String created=client().post().uri("/boxes").header(HttpHeaders.AUTHORIZATION,auth).contentType(MediaType.APPLICATION_JSON).body("{\"name\":\"Viagem\",\"targetAmount\":\"1000.00\"}").retrieve().body(String.class);
		assertThat(created).contains("Viagem").contains("1000.00");
		String id=created.substring(created.indexOf("\"id\":\"")+6); id=id.substring(0,id.indexOf('"'));
		String updated=client().patch().uri("/boxes/"+id).header(HttpHeaders.AUTHORIZATION,auth).contentType(MediaType.APPLICATION_JSON).body("{\"name\":\"Férias\",\"targetAmount\":\"1200.00\"}").retrieve().body(String.class);
		assertThat(updated).contains("Férias");
		String movements=client().get().uri("/boxes/"+id+"/movements").header(HttpHeaders.AUTHORIZATION,auth).retrieve().body(String.class);
		assertThat(movements).contains("totalElements");
		client().delete().uri("/boxes/"+id).header(HttpHeaders.AUTHORIZATION,auth).retrieve().toBodilessEntity();
	}
}
