package leoszr.finance_app.budget.entity;

import jakarta.persistence.*;
import leoszr.finance_app.category.entity.Category;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="budget_categories", uniqueConstraints=@UniqueConstraint(name="uk_budget_category", columnNames={"budget_id","category_id"}))
public class BudgetCategory {
	@Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id;
	@ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="budget_id", nullable=false) private Budget budget;
	@ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="category_id", nullable=false) private Category category;
	@Column(nullable=false, precision=14, scale=2) private BigDecimal amount;
	@Column(name="created_at", nullable=false, updatable=false) private Instant createdAt;
	@PrePersist void prePersist(){createdAt=Instant.now();}
	public UUID getId(){return id;} public Budget getBudget(){return budget;} public void setBudget(Budget v){budget=v;} public Category getCategory(){return category;} public void setCategory(Category v){category=v;} public BigDecimal getAmount(){return amount;} public void setAmount(BigDecimal v){amount=v;}
}
