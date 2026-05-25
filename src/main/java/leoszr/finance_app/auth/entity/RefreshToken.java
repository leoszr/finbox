package leoszr.finance_app.auth.entity;

import jakarta.persistence.*;
import leoszr.finance_app.user.entity.User;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	@ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "user_id", nullable = false)
	private User user;
	@Column(name = "token_hash", nullable = false, unique = true)
	private String tokenHash;
	@Column(name = "expires_at", nullable = false)
	private Instant expiresAt;
	@Column(name = "revoked_at")
	private Instant revokedAt;
	@OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "replaced_by_token_id")
	private RefreshToken replacedByToken;
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;
	@Column(name = "created_by_ip")
	private String createdByIp;
	@Column(name = "user_agent")
	private String userAgent;
	@PrePersist void prePersist(){ createdAt=Instant.now(); }
	public UUID getId(){return id;} public User getUser(){return user;} public void setUser(User v){user=v;}
	public String getTokenHash(){return tokenHash;} public void setTokenHash(String v){tokenHash=v;}
	public Instant getExpiresAt(){return expiresAt;} public void setExpiresAt(Instant v){expiresAt=v;}
	public Instant getRevokedAt(){return revokedAt;} public void setRevokedAt(Instant v){revokedAt=v;}
	public RefreshToken getReplacedByToken(){return replacedByToken;} public void setReplacedByToken(RefreshToken v){replacedByToken=v;}
	public Instant getCreatedAt(){return createdAt;} public String getCreatedByIp(){return createdByIp;} public void setCreatedByIp(String v){createdByIp=v;}
	public String getUserAgent(){return userAgent;} public void setUserAgent(String v){userAgent=v;}
	public boolean isActive(){ return revokedAt == null && expiresAt.isAfter(Instant.now()); }
}
