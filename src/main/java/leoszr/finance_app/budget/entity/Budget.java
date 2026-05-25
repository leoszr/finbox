package leoszr.finance_app.budget.entity;

import jakarta.persistence.*;
import leoszr.finance_app.user.entity.User;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

@Entity
@Table(name="budgets")
public class Budget {
	@Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id;
	@ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="user_id", nullable=false) private User user;
	@Column(name="total_amount", nullable=false, precision=14, scale=2) private BigDecimal totalAmount;
	@Enumerated(EnumType.STRING) @Column(name="cycle_type", nullable=false) private BudgetCycleType cycleType;
	@Column(name="cycle_start_date", nullable=false) private LocalDate cycleStartDate;
	@Column(nullable=false) private boolean active=true;
	@OneToMany(mappedBy="budget", cascade=CascadeType.ALL, orphanRemoval=true) private List<BudgetCategory> categories=new ArrayList<>();
	@Column(name="created_at", nullable=false, updatable=false) private Instant createdAt;
	@Column(name="updated_at", nullable=false) private Instant updatedAt;
	@PrePersist void prePersist(){Instant now=Instant.now();createdAt=now;updatedAt=now;} @PreUpdate void preUpdate(){updatedAt=Instant.now();}
	public UUID getId(){return id;} public User getUser(){return user;} public void setUser(User v){user=v;} public BigDecimal getTotalAmount(){return totalAmount;} public void setTotalAmount(BigDecimal v){totalAmount=v;}
	public BudgetCycleType getCycleType(){return cycleType;} public void setCycleType(BudgetCycleType v){cycleType=v;} public LocalDate getCycleStartDate(){return cycleStartDate;} public void setCycleStartDate(LocalDate v){cycleStartDate=v;}
	public boolean isActive(){return active;} public void setActive(boolean v){active=v;} public List<BudgetCategory> getCategories(){return categories;} public void setCategories(List<BudgetCategory> v){categories=v;}
}
