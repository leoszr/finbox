package leoszr.finance_app.security;

import leoszr.finance_app.common.UnauthorizedException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class MockAuthTokenServiceTest {
	private final MockAuthTokenService service = new MockAuthTokenService();
	@Test void validTokenReturnsFields() {
		assertThat(service.verify("uid|u@e.com|User")).isEqualTo(new AuthToken("uid", "u@e.com", "User"));
	}
	@Test void invalidTokenThrowsUnauthorized() {
		assertThatThrownBy(() -> service.verify("invalid-token")).isInstanceOf(UnauthorizedException.class);
	}
	@Test void missingEmailThrowsUnauthorized() {
		assertThatThrownBy(() -> service.verify("uid||User")).isInstanceOf(UnauthorizedException.class);
	}
}
