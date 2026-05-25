package leoszr.finance_app.user.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_preferences")
public class UserPreferences {
	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	@OneToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;
	@Enumerated(EnumType.STRING) @Column(name = "default_period", nullable = false)
	private DefaultPeriod defaultPeriod = DefaultPeriod.MONTHLY;
	@Enumerated(EnumType.STRING) @Column(name = "default_sort", nullable = false)
	private DefaultSort defaultSort = DefaultSort.NEWEST_FIRST;
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;
	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;
	@PrePersist void prePersist(){ Instant now=Instant.now(); createdAt=now; updatedAt=now; }
	@PreUpdate void preUpdate(){ updatedAt=Instant.now(); }
	public UUID getId(){return id;} public User getUser(){return user;} public void setUser(User v){user=v;}
	public DefaultPeriod getDefaultPeriod(){return defaultPeriod;} public void setDefaultPeriod(DefaultPeriod v){defaultPeriod=v;}
	public DefaultSort getDefaultSort(){return defaultSort;} public void setDefaultSort(DefaultSort v){defaultSort=v;}
}
