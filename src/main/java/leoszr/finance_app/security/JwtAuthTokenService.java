package leoszr.finance_app.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import leoszr.finance_app.common.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;

@Service
@Primary
public class JwtAuthTokenService implements AuthTokenService {
	private final ObjectMapper mapper;
	private final String secret;
	private final String issuer;
	private final long accessTtlSeconds;
	private final Base64.Encoder urlEncoder = Base64.getUrlEncoder().withoutPadding();
	private final Base64.Decoder urlDecoder = Base64.getUrlDecoder();

	public JwtAuthTokenService(ObjectMapper mapper,
			@Value("${auth.jwt.secret:dev-secret-change-me-dev-secret-change-me}") String secret,
			@Value("${auth.jwt.issuer:finance-app}") String issuer,
			@Value("${auth.jwt.access-ttl-seconds:900}") long accessTtlSeconds) {
		this.mapper = mapper; this.secret = secret; this.issuer = issuer; this.accessTtlSeconds = accessTtlSeconds;
	}

	public String issue(String userId, String email, String name) {
		try {
			long now = Instant.now().getEpochSecond();
			String header = enc(mapper.writeValueAsBytes(Map.of("alg", "HS256", "typ", "JWT")));
			String payload = enc(mapper.writeValueAsBytes(Map.of("sub", userId, "email", email, "name", name, "iss", issuer, "iat", now, "exp", now + accessTtlSeconds)));
			return header + "." + payload + "." + sign(header + "." + payload);
		} catch (Exception e) { throw new IllegalStateException("Não foi possível gerar token.", e); }
	}

	@Override public AuthToken verify(String bearerToken) {
		try {
			String[] p = bearerToken.split("\\.");
			if (p.length != 3 || !sign(p[0] + "." + p[1]).equals(p[2])) throw new UnauthorizedException("Token inválido.");
			Map<String, Object> claims = mapper.readValue(urlDecoder.decode(p[1]), new TypeReference<>() {});
			if (!issuer.equals(claims.get("iss"))) throw new UnauthorizedException("Token inválido.");
			Number exp = (Number) claims.get("exp");
			if (exp == null || exp.longValue() <= Instant.now().getEpochSecond()) throw new UnauthorizedException("Token expirado.");
			return new AuthToken((String) claims.get("sub"), (String) claims.get("email"), (String) claims.getOrDefault("name", ""));
		} catch (UnauthorizedException e) { throw e; }
		catch (Exception e) { throw new UnauthorizedException("Token inválido."); }
	}

	public long accessTtlSeconds() { return accessTtlSeconds; }
	private String enc(byte[] bytes) { return urlEncoder.encodeToString(bytes); }
	private String sign(String data) throws Exception {
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
		return enc(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
	}
}
