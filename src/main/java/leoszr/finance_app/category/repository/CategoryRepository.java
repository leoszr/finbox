package leoszr.finance_app.category.repository;

import leoszr.finance_app.category.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
	Optional<Category> findByIdAndUserId(UUID id, UUID userId);
	boolean existsByUserIdAndNormalizedName(UUID userId, String normalizedName);
	boolean existsByUserIdAndNormalizedNameAndIdNot(UUID userId, String normalizedName, UUID id);
	List<Category> findByUserIdOrderBySpecialTypeAscNameAsc(UUID userId);
	Optional<Category> findByUserIdAndSpecialType(UUID userId, CategorySpecialType specialType);
}
