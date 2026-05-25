package leoszr.finance_app.dashboard.service;

import leoszr.finance_app.budget.dto.BudgetUsageResponse;
import leoszr.finance_app.budget.service.BudgetService;
import leoszr.finance_app.common.NotFoundException;
import leoszr.finance_app.dashboard.dto.*;
import leoszr.finance_app.savingbox.repository.SavingBoxRepository;
import leoszr.finance_app.transaction.dto.TransactionResponse;
import leoszr.finance_app.transaction.entity.TransactionType;
import leoszr.finance_app.transaction.repository.TransactionRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class DashboardService {
	private final BudgetService budgets; private final TransactionRepository transactions; private final SavingBoxRepository boxes;
	public DashboardService(BudgetService budgets, TransactionRepository transactions, SavingBoxRepository boxes){this.budgets=budgets;this.transactions=transactions;this.boxes=boxes;}
	@Transactional(readOnly=true) public DashboardResponse build(UUID userId, LocalDate referenceDate){
		LocalDate ref=referenceDate==null?LocalDate.now():referenceDate;
		BudgetUsageResponse usage; try { usage=budgets.currentCycle(userId, ref); } catch(NotFoundException e) { usage=null; }
		LocalDate start=usage==null?ref.withDayOfMonth(1):usage.startDate(); LocalDate end=usage==null?ref:usage.endDate();
		BigDecimal income=transactions.sumByTypeInPeriod(userId, TransactionType.INCOME, start, end); BigDecimal expenses=transactions.sumByTypeInPeriod(userId, TransactionType.EXPENSE, start, end); BigDecimal saved=boxes.sumActiveBalanceByUserId(userId);
		var summary=new DashboardSummaryResponse(income, expenses, income.subtract(expenses), saved);
		var budget=usage==null?null:new DashboardBudgetResponse(usage.totalAmount(), usage.usedAmount(), usage.status(), usage.startDate(), usage.endDate());
		var latest=transactions.findLatestByUserId(userId, PageRequest.of(0,5,Sort.by("occurredOn").descending())).map(t -> new TransactionResponse(t.getId(),t.getType(),t.getAmount(),t.getOccurredOn(),t.getDescription(),t.getCategory().getId(),t.getCategory().getName(),t.getCategory().getSpecialType(),t.getBoxId())).toList();
		return new DashboardResponse(summary,budget,latest);
	}
}
