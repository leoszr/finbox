package leoszr.finance_app.savingbox.repository;

import leoszr.finance_app.savingbox.entity.SavingBox;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;

public interface SavingBoxRepository extends JpaRepository<SavingBox, UUID> {
	List<SavingBox> findByUserIdAndDeletedAtIsNullOrderByDefaultBoxDescNameAsc(UUID userId);
	Optional<SavingBox> findByIdAndUserIdAndDeletedAtIsNull(UUID id, UUID userId);
	Optional<SavingBox> findByUserIdAndDefaultBoxTrueAndDeletedAtIsNull(UUID userId);
	boolean existsByUserIdAndNormalizedNameAndDeletedAtIsNull(UUID userId, String normalizedName);
	boolean existsByUserIdAndNormalizedNameAndDeletedAtIsNullAndIdNot(UUID userId, String normalizedName, UUID id);
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select b from SavingBox b where b.id = :id and b.user.id = :userId and b.deletedAt is null")
	Optional<SavingBox> findActiveByIdAndUserIdForUpdate(@Param("id") UUID id, @Param("userId") UUID userId);
	@Query("select coalesce(sum(b.balance), 0) from SavingBox b where b.user.id = :userId and b.deletedAt is null")
	java.math.BigDecimal sumActiveBalanceByUserId(@Param("userId") UUID userId);
}
