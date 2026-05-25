package leoszr.finance_app.dashboard.dto;

import leoszr.finance_app.budget.entity.BudgetStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public record DashboardBudgetResponse(BigDecimal totalAmount, BigDecimal usedAmount, BudgetStatus status, LocalDate startDate, LocalDate endDate) {}
