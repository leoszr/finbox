package leoszr.finance_app.transaction.repository;

import leoszr.finance_app.transaction.entity.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
	Optional<Transaction> findByIdAndUserId(UUID id, UUID userId);
	@Query("""
		select t from Transaction t join fetch t.category c where t.user.id = :userId
		and (:startDate is null or t.occurredOn >= :startDate)
		and (:endDate is null or t.occurredOn <= :endDate)
		and (:categoryId is null or c.id = :categoryId)
		and (:type is null or t.type = :type)
		and (:minAmount is null or t.amount >= :minAmount)
		and (:maxAmount is null or t.amount <= :maxAmount)
		and (:description is null or lower(t.description) like lower(concat('%', :description, '%')))
		""")
	Page<Transaction> search(@Param("userId") UUID userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("categoryId") UUID categoryId, @Param("type") TransactionType type, @Param("minAmount") BigDecimal minAmount, @Param("maxAmount") BigDecimal maxAmount, @Param("description") String description, Pageable pageable);
	@Query("select coalesce(sum(t.amount), 0) from Transaction t where t.user.id = :userId and t.type = leoszr.finance_app.transaction.entity.TransactionType.EXPENSE and t.occurredOn between :startDate and :endDate")
	BigDecimal sumExpensesInPeriod(@Param("userId") UUID userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
	@Query("select t from Transaction t join fetch t.category where t.user.id = :userId and t.boxId = :boxId")
	Page<Transaction> findMovements(@Param("userId") UUID userId, @Param("boxId") UUID boxId, Pageable pageable);
	@Query("select coalesce(sum(t.amount), 0) from Transaction t where t.user.id = :userId and t.type = :type and t.occurredOn between :startDate and :endDate")
	BigDecimal sumByTypeInPeriod(@Param("userId") UUID userId, @Param("type") TransactionType type, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
	@Query("select t from Transaction t join fetch t.category where t.user.id = :userId")
	Page<Transaction> findLatestByUserId(@Param("userId") UUID userId, Pageable pageable);
}
