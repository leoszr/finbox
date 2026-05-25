package leoszr.finance_app.security;

import jakarta.validation.Validation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthTokenTest {
	@Test void validatesRequiredFields() {
		try (var factory = Validation.buildDefaultValidatorFactory()) {
			var violations = factory.getValidator().validate(new AuthToken("", "bad", null));
			assertThat(violations).hasSizeGreaterThanOrEqualTo(2);
		}
	}
}
