package leoszr.finance_app.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthToken(
		@NotBlank String externalAuthId,
		@NotBlank @Email String email,
		String name
) {
	public AuthToken {
		if (name == null) {
			name = "";
		}
	}
}
