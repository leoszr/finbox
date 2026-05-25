package leoszr.finance_app.security;

public interface AuthTokenService {
	AuthToken verify(String bearerToken);
}
