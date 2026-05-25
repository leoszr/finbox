package leoszr.finance_app.budget.dto;

import leoszr.finance_app.budget.entity.BudgetStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record BudgetUsageResponse(UUID budgetId, LocalDate startDate, LocalDate endDate, BigDecimal totalAmount, BigDecimal usedAmount, BudgetStatus status) {}
