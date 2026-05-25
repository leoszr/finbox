package leoszr.finance_app.auth.service;

import leoszr.finance_app.auth.dto.*;
import leoszr.finance_app.auth.entity.RefreshToken;
import leoszr.finance_app.auth.repository.RefreshTokenRepository;
import leoszr.finance_app.category.entity.*;
import leoszr.finance_app.category.repository.CategoryRepository;
import leoszr.finance_app.common.BusinessRuleException;
import leoszr.finance_app.common.TextNormalizer;
import leoszr.finance_app.common.UnauthorizedException;
import leoszr.finance_app.security.JwtAuthTokenService;
import leoszr.finance_app.user.entity.*;
import leoszr.finance_app.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

@Service
public class AuthService {
	private final UserRepository users; private final CategoryRepository categories; private final RefreshTokenRepository refreshTokens;
	private final PasswordEncoder passwordEncoder; private final JwtAuthTokenService jwt; private final SecureRandom random = new SecureRandom();
	private final long refreshTtlSeconds;
	public AuthService(UserRepository users, CategoryRepository categories, RefreshTokenRepository refreshTokens, PasswordEncoder passwordEncoder, JwtAuthTokenService jwt,
			@Value("${auth.jwt.refresh-ttl-seconds:2592000}") long refreshTtlSeconds) {
		this.users=users; this.categories=categories; this.refreshTokens=refreshTokens; this.passwordEncoder=passwordEncoder; this.jwt=jwt; this.refreshTtlSeconds=refreshTtlSeconds;
	}
	@Transactional public AuthResponse register(RegisterRequest req, String ip, String ua) {
		String email = req.email().trim().toLowerCase();
		if (users.existsByEmailIgnoreCase(email)) throw new BusinessRuleException("Email já cadastrado.");
		User user = new User(); user.setEmail(email); user.setName(req.name().trim()); user.setPasswordHash(passwordEncoder.encode(req.password())); user.setEmailVerified(false); user.setStatus(UserStatus.ACTIVE); user.setFirebaseUid("local:" + email); user.setPreferences(new UserPreferences());
		User saved = users.save(user); createSpecial(saved, "Não categorizado", "#9CA3AF", CategoryType.BOTH, CategorySpecialType.UNCATEGORIZED); createSpecial(saved, "Guardado", "#22C55E", CategoryType.EXPENSE, CategorySpecialType.SAVED);
		return issue(saved, ip, ua);
	}
	@Transactional public AuthResponse login(LoginRequest req, String ip, String ua) {
		User user = users.findByEmailIgnoreCase(req.email().trim().toLowerCase()).orElseThrow(() -> new UnauthorizedException("Credenciais inválidas."));
		if (user.getStatus() != UserStatus.ACTIVE || user.getPasswordHash() == null || !passwordEncoder.matches(req.password(), user.getPasswordHash())) throw new UnauthorizedException("Credenciais inválidas.");
		return issue(user, ip, ua);
	}
	@Transactional public AuthResponse refresh(String raw, String ip, String ua) {
		RefreshToken old = refreshTokens.findByTokenHash(hash(raw)).orElseThrow(() -> new UnauthorizedException("Refresh token inválido."));
		if (!old.isActive()) throw new UnauthorizedException("Refresh token inválido.");
		AuthResponse next = issue(old.getUser(), ip, ua); old.setRevokedAt(Instant.now()); return next;
	}
	@Transactional public void logout(String raw) { refreshTokens.findByTokenHash(hash(raw)).ifPresent(t -> { if (t.getRevokedAt()==null) t.setRevokedAt(Instant.now()); }); }
	private AuthResponse issue(User user, String ip, String ua) { String raw = randomToken(); RefreshToken rt = new RefreshToken(); rt.setUser(user); rt.setTokenHash(hash(raw)); rt.setExpiresAt(Instant.now().plusSeconds(refreshTtlSeconds)); rt.setCreatedByIp(ip); rt.setUserAgent(ua == null ? null : ua.substring(0, Math.min(ua.length(), 512))); refreshTokens.save(rt); return new AuthResponse(jwt.issue(user.getId().toString(), user.getEmail(), user.getName()), raw, jwt.accessTtlSeconds()); }
	private String randomToken(){ byte[] b=new byte[64]; random.nextBytes(b); return Base64.getUrlEncoder().withoutPadding().encodeToString(b); }
	private String hash(String raw) { try { MessageDigest d=MessageDigest.getInstance("SHA-256"); return Base64.getEncoder().encodeToString(d.digest(raw.getBytes(StandardCharsets.UTF_8))); } catch(Exception e){ throw new IllegalStateException(e); } }
	private void createSpecial(User user, String name, String color, CategoryType type, CategorySpecialType specialType) { Category c=new Category(); c.setUser(user); c.setName(name); c.setNormalizedName(TextNormalizer.normalize(name)); c.setColor(color); c.setType(type); c.setSpecialType(specialType); categories.save(c); }
}
