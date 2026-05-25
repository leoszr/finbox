package leoszr.finance_app.security;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import leoszr.finance_app.user.entity.UserRole;
import static org.assertj.core.api.Assertions.assertThat;

class AuthenticatedUserTest {
	@Test void exposesFieldsAndAuthority() {
		var id = UUID.randomUUID();
		var user = new AuthenticatedUser(id, "uid", "a@b.com", "Ana", UserRole.USER);
		assertThat(user.userId()).isEqualTo(id);
		assertThat(user.getUsername()).isEqualTo("a@b.com");
		assertThat(user.getAuthorities()).extracting("authority").containsExactly("ROLE_USER");
	}
}
