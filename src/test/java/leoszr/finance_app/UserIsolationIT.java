package leoszr.finance_app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.*;
import static org.assertj.core.api.Assertions.*;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;

@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserIsolationIT {
	@LocalServerPort int port;
	RestClient client(){ return RestClient.builder().baseUrl("http://localhost:"+port).build(); }
	@Test void usersCannotAccessOtherUsersResources(){
		String a="Bearer iso-a|a@example.com|A", b="Bearer iso-b|b@example.com|B";
		String cat=createCategory(a); String catId=id(cat);
		String tx=client().post().uri("/transactions").header(HttpHeaders.AUTHORIZATION,a).contentType(MediaType.APPLICATION_JSON).body("{\"type\":\"EXPENSE\",\"amount\":\"10.00\",\"occurredOn\":\"2026-05-01\",\"categoryId\":\""+catId+"\"}").retrieve().body(String.class); String txId=id(tx);
		String box=client().post().uri("/boxes").header(HttpHeaders.AUTHORIZATION,a).contentType(MediaType.APPLICATION_JSON).body("{\"name\":\"Privada\",\"targetAmount\":\"100.00\"}").retrieve().body(String.class); String boxId=id(box);
		client().post().uri("/budget").header(HttpHeaders.AUTHORIZATION,a).contentType(MediaType.APPLICATION_JSON).body("{\"totalAmount\":\"100.00\",\"cycleType\":\"MONTHLY\",\"cycleStartDate\":\"2026-05-01\",\"categories\":[]}").retrieve().body(String.class);
		assertNotFound(() -> client().patch().uri("/categories/"+catId).header(HttpHeaders.AUTHORIZATION,b).contentType(MediaType.APPLICATION_JSON).body("{\"name\":\"Hack\",\"color\":\"#222\",\"type\":\"EXPENSE\"}").retrieve().body(String.class));
		assertNotFound(() -> client().get().uri("/transactions/"+txId).header(HttpHeaders.AUTHORIZATION,b).retrieve().body(String.class));
		assertNotFound(() -> client().get().uri("/boxes/"+boxId).header(HttpHeaders.AUTHORIZATION,b).retrieve().body(String.class));
		String dashB=client().get().uri("/dashboard?referenceDate=2026-05-01").header(HttpHeaders.AUTHORIZATION,b).retrieve().body(String.class);
		assertThat(dashB).doesNotContain("Privada").doesNotContain("10.00");
		assertNotFound(() -> client().get().uri("/budget").header(HttpHeaders.AUTHORIZATION,b).retrieve().body(String.class));
	}
	private String createCategory(String auth){ return client().post().uri("/categories").header(HttpHeaders.AUTHORIZATION,auth).contentType(MediaType.APPLICATION_JSON).body("{\"name\":\"Isolada\",\"color\":\"#111\",\"type\":\"EXPENSE\"}").retrieve().body(String.class); }
	private String id(String json){ String id=json.substring(json.indexOf("\"id\":\"")+6); return id.substring(0,id.indexOf('"')); }
	private void assertNotFound(ThrowingCallable call){ assertThatThrownBy(call).isInstanceOf(HttpClientErrorException.NotFound.class); }
}
