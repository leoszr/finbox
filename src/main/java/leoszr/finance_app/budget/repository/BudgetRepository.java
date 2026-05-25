package leoszr.finance_app.budget.repository;

import leoszr.finance_app.budget.entity.Budget;
import org.springframework.data.jpa.repository.*;
import java.util.*;

public interface BudgetRepository extends JpaRepository<Budget, UUID> {
	@Query("select distinct b from Budget b left join fetch b.categories bc left join fetch bc.category where b.user.id = :userId and b.active = true")
	Optional<Budget> findActiveByUserId(UUID userId);
	boolean existsByUserIdAndActiveTrue(UUID userId);
}
