package leoszr.finance_app.security;

import leoszr.finance_app.common.UnauthorizedException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Profile({"local", "test"})
public class MockAuthTokenService implements AuthTokenService {
	@Override
	public AuthToken verify(String bearerToken) {
		if (!StringUtils.hasText(bearerToken) || bearerToken.startsWith("invalid")) {
			throw new UnauthorizedException("Token inválido.");
		}
		String[] parts = bearerToken.split("\\|", -1);
		String uid = parts[0];
		String email = parts.length > 1 ? parts[1] : uid + "@example.com";
		String name = parts.length > 2 ? parts[2] : "";
		if (!StringUtils.hasText(email)) {
			throw new UnauthorizedException("Token sem email.");
		}
		return new AuthToken(uid, email, name);
	}
}
