package leoszr.finance_app.dashboard.dto;

import leoszr.finance_app.transaction.dto.TransactionResponse;
import java.util.List;

public record DashboardResponse(DashboardSummaryResponse summary, DashboardBudgetResponse budget, List<TransactionResponse> latestTransactions) {}
