package leoszr.finance_app.savingbox.entity;

import jakarta.persistence.*;
import leoszr.finance_app.user.entity.User;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="saving_boxes")
public class SavingBox {
	@Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id;
	@ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="user_id", nullable=false) private User user;
	@Column(nullable=false,length=80) private String name;
	@Column(name="normalized_name", nullable=false,length=80) private String normalizedName;
	@Column(nullable=false,length=3) private String currency="BRL";
	@Column(nullable=false,precision=14,scale=2) private BigDecimal balance=BigDecimal.ZERO;
	@Column(name="target_amount",precision=14,scale=2) private BigDecimal targetAmount;
	@Column(name="default_box",nullable=false) private boolean defaultBox;
	@Column(name="created_at",nullable=false,updatable=false) private Instant createdAt;
	@Column(name="updated_at",nullable=false) private Instant updatedAt;
	@Column(name="deleted_at") private Instant deletedAt;
	@PrePersist void prePersist(){Instant now=Instant.now();createdAt=now;updatedAt=now;} @PreUpdate void preUpdate(){updatedAt=Instant.now();}
	public UUID getId(){return id;} public User getUser(){return user;} public void setUser(User v){user=v;} public String getName(){return name;} public void setName(String v){name=v;} public String getNormalizedName(){return normalizedName;} public void setNormalizedName(String v){normalizedName=v;}
	public String getCurrency(){return currency;} public void setCurrency(String v){currency=v;} public BigDecimal getBalance(){return balance;} public void setBalance(BigDecimal v){balance=v;} public BigDecimal getTargetAmount(){return targetAmount;} public void setTargetAmount(BigDecimal v){targetAmount=v;}
	public boolean isDefaultBox(){return defaultBox;} public void setDefaultBox(boolean v){defaultBox=v;} public Instant getDeletedAt(){return deletedAt;} public void setDeletedAt(Instant v){deletedAt=v;}
}
