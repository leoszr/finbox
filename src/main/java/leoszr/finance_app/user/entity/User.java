package leoszr.finance_app.user.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	@Column(name = "firebase_uid", nullable = false, unique = true)
	private String externalAuthId;
	@Column(nullable = false)
	private String email;
	@Column(name = "password_hash")
	private String passwordHash;
	@Column(name = "email_verified", nullable = false)
	private boolean emailVerified;
	@Enumerated(EnumType.STRING) @Column(nullable = false)
	private UserStatus status = UserStatus.ACTIVE;
	@Enumerated(EnumType.STRING) @Column(nullable = false)
	private UserRole role = UserRole.USER;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String currency = "BRL";
	@Enumerated(EnumType.STRING) @Column(nullable = false)
	private Theme theme = Theme.SYSTEM;
	@Column(name = "face_id_enabled", nullable = false)
	private boolean faceIdEnabled;
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;
	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private UserPreferences preferences;
	@PrePersist void prePersist(){ Instant now=Instant.now(); createdAt=now; updatedAt=now; }
	@PreUpdate void preUpdate(){ updatedAt=Instant.now(); }
	public UUID getId(){return id;} public String getFirebaseUid(){return externalAuthId;} public void setFirebaseUid(String v){externalAuthId=v;}
	public String getEmail(){return email;} public void setEmail(String v){email=v;} public String getPasswordHash(){return passwordHash;} public void setPasswordHash(String v){passwordHash=v;}
	public boolean isEmailVerified(){return emailVerified;} public void setEmailVerified(boolean v){emailVerified=v;} public UserStatus getStatus(){return status;} public void setStatus(UserStatus v){status=v;}
	public UserRole getRole(){return role;} public void setRole(UserRole v){role=v;}
	public String getName(){return name;} public void setName(String v){name=v;}
	public String getCurrency(){return currency;} public void setCurrency(String v){currency=v;} public Theme getTheme(){return theme;} public void setTheme(Theme v){theme=v;}
	public boolean isFaceIdEnabled(){return faceIdEnabled;} public void setFaceIdEnabled(boolean v){faceIdEnabled=v;} public UserPreferences getPreferences(){return preferences;}
	public void setPreferences(UserPreferences p){preferences=p; if(p!=null)p.setUser(this);} public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;}
}
