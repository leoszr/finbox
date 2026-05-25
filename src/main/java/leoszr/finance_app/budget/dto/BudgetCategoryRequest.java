package leoszr.finance_app.budget.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

public record BudgetCategoryRequest(@NotNull UUID categoryId, @NotNull @DecimalMin("0.01") @Digits(integer=12, fraction=2) BigDecimal amount) {}
