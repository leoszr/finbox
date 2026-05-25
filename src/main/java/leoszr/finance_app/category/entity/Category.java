package leoszr.finance_app.category.entity;

import jakarta.persistence.*;
import leoszr.finance_app.user.entity.User;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "categories", uniqueConstraints = @UniqueConstraint(name = "uk_category_name_per_user", columnNames = {"user_id", "normalized_name"}))
public class Category {
	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	@ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "user_id", nullable = false)
	private User user;
	@Column(nullable = false, length = 80) private String name;
	@Column(name = "normalized_name", nullable = false, length = 80) private String normalizedName;
	@Column(nullable = false, length = 20) private String color;
	@Enumerated(EnumType.STRING) @Column(nullable = false) private CategoryType type;
	@Enumerated(EnumType.STRING) @Column(name = "special_type", nullable = false) private CategorySpecialType specialType = CategorySpecialType.CUSTOM;
	@Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt;
	@Column(name = "updated_at", nullable = false) private Instant updatedAt;
	@PrePersist void prePersist(){ Instant now=Instant.now(); createdAt=now; updatedAt=now; }
	@PreUpdate void preUpdate(){ updatedAt=Instant.now(); }
	public UUID getId(){return id;} public User getUser(){return user;} public void setUser(User v){user=v;}
	public String getName(){return name;} public void setName(String v){name=v;} public String getNormalizedName(){return normalizedName;} public void setNormalizedName(String v){normalizedName=v;}
	public String getColor(){return color;} public void setColor(String v){color=v;} public CategoryType getType(){return type;} public void setType(CategoryType v){type=v;}
	public CategorySpecialType getSpecialType(){return specialType;} public void setSpecialType(CategorySpecialType v){specialType=v;}
}
