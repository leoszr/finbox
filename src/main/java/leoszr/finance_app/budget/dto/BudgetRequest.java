package leoszr.finance_app.budget.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import leoszr.finance_app.budget.entity.BudgetCycleType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record BudgetRequest(@NotNull @DecimalMin("0.01") @Digits(integer=12, fraction=2) BigDecimal totalAmount, @NotNull BudgetCycleType cycleType, @NotNull LocalDate cycleStartDate, @Valid List<BudgetCategoryRequest> categories) {}
