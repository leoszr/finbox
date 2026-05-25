package leoszr.finance_app.transaction.entity;

import jakarta.persistence.*;
import leoszr.finance_app.category.entity.Category;
import leoszr.finance_app.user.entity.User;

import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class Transaction {
	@Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
	@ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "user_id", nullable = false) private User user;
	@ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "category_id", nullable = false) private Category category;
	@Column(name = "box_id") private UUID boxId;
	@Enumerated(EnumType.STRING) @Column(nullable = false) private TransactionType type;
	@Column(nullable = false, precision = 14, scale = 2) private BigDecimal amount;
	@Column(name = "occurred_on", nullable = false) private LocalDate occurredOn;
	@Column(length = 255) private String description;
	@Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt;
	@Column(name = "updated_at", nullable = false) private Instant updatedAt;
	@PrePersist void prePersist(){ Instant now=Instant.now(); createdAt=now; updatedAt=now; }
	@PreUpdate void preUpdate(){ updatedAt=Instant.now(); }
	public UUID getId(){return id;} public User getUser(){return user;} public void setUser(User v){user=v;}
	public Category getCategory(){return category;} public void setCategory(Category v){category=v;} public UUID getBoxId(){return boxId;} public void setBoxId(UUID v){boxId=v;}
	public TransactionType getType(){return type;} public void setType(TransactionType v){type=v;} public BigDecimal getAmount(){return amount;} public void setAmount(BigDecimal v){amount=v;}
	public LocalDate getOccurredOn(){return occurredOn;} public void setOccurredOn(LocalDate v){occurredOn=v;} public String getDescription(){return description;} public void setDescription(String v){description=v;}
}
