package leoszr.finance_app.security;

import leoszr.finance_app.common.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Profile("supabase")
public class SupabaseAuthTokenService implements AuthTokenService {
	private final JwtDecoder decoder;
	private final String issuer;
	public SupabaseAuthTokenService(@Value("${supabase.project-ref}") String projectRef) {
		if (!StringUtils.hasText(projectRef)) throw new IllegalStateException("supabase.project-ref obrigatório.");
		this.issuer = "https://" + projectRef + ".supabase.co/auth/v1";
		this.decoder = NimbusJwtDecoder.withJwkSetUri(issuer + "/.well-known/jwks.json").build();
	}
	@Override public AuthToken verify(String bearerToken) {
		try {
			Jwt jwt = decoder.decode(bearerToken);
			if (!issuer.equals(jwt.getIssuer().toString())) throw new UnauthorizedException("Token inválido.");
			if (!jwt.getAudience().contains("authenticated")) throw new UnauthorizedException("Token inválido.");
			String sub = jwt.getSubject(); String email = jwt.getClaimAsString("email");
			String name = jwt.getClaimAsMap("user_metadata") == null ? "" : String.valueOf(jwt.getClaimAsMap("user_metadata").getOrDefault("name", ""));
			if (!StringUtils.hasText(sub) || !StringUtils.hasText(email)) throw new UnauthorizedException("Token inválido.");
			return new AuthToken(sub, email, name);
		} catch (UnauthorizedException e) { throw e; }
		catch (Exception e) { throw new UnauthorizedException("Token inválido."); }
	}
}
