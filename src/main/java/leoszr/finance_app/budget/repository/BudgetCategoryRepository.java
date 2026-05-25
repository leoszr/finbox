package leoszr.finance_app.budget.repository;

import leoszr.finance_app.budget.entity.BudgetCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface BudgetCategoryRepository extends JpaRepository<BudgetCategory, UUID> {}
