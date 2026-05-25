package leoszr.finance_app.transaction.dto;

import jakarta.validation.constraints.*;
import leoszr.finance_app.transaction.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionRequest(
		@NotNull TransactionType type,
		@NotNull @DecimalMin("0.01") @Digits(integer = 12, fraction = 2) BigDecimal amount,
		@NotNull LocalDate occurredOn,
		UUID categoryId,
		UUID boxId,
		@Size(max = 255) String description
) {}
