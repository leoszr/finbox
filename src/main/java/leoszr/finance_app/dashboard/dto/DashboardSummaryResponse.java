package leoszr.finance_app.dashboard.dto;

import java.math.BigDecimal;

public record DashboardSummaryResponse(BigDecimal income, BigDecimal expenses, BigDecimal balance, BigDecimal savedTotal) {}
