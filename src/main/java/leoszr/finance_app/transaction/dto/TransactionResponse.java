package leoszr.finance_app.transaction.dto;

import leoszr.finance_app.category.entity.CategorySpecialType;
import leoszr.finance_app.transaction.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionResponse(UUID id, TransactionType type, BigDecimal amount, LocalDate occurredOn, String description, UUID categoryId, String categoryName, CategorySpecialType categorySpecialType, UUID boxId) {}
