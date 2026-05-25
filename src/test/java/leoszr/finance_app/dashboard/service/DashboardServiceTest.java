package leoszr.finance_app.dashboard.service;

import leoszr.finance_app.budget.dto.BudgetUsageResponse;
import leoszr.finance_app.budget.entity.BudgetStatus;
import leoszr.finance_app.budget.service.BudgetService;
import leoszr.finance_app.savingbox.repository.SavingBoxRepository;
import leoszr.finance_app.transaction.entity.TransactionType;
import leoszr.finance_app.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class DashboardServiceTest {
	@Mock BudgetService budgets; @Mock TransactionRepository transactions; @Mock SavingBoxRepository boxes; @InjectMocks DashboardService service;
	@Test void buildDashboard(){ UUID userId=UUID.randomUUID(); when(budgets.currentCycle(eq(userId), any())).thenReturn(new BudgetUsageResponse(UUID.randomUUID(), LocalDate.parse("2026-05-01"), LocalDate.parse("2026-05-31"), new BigDecimal("500.00"), new BigDecimal("100.00"), BudgetStatus.NORMAL)); when(transactions.sumByTypeInPeriod(userId, TransactionType.INCOME, LocalDate.parse("2026-05-01"), LocalDate.parse("2026-05-31"))).thenReturn(new BigDecimal("1000.00")); when(transactions.sumByTypeInPeriod(userId, TransactionType.EXPENSE, LocalDate.parse("2026-05-01"), LocalDate.parse("2026-05-31"))).thenReturn(new BigDecimal("100.00")); when(boxes.sumActiveBalanceByUserId(userId)).thenReturn(new BigDecimal("50.00")); when(transactions.findLatestByUserId(eq(userId), any())).thenReturn(Page.empty()); var res=service.build(userId, LocalDate.parse("2026-05-10")); assertThat(res.summary().balance()).isEqualByComparingTo("900.00"); assertThat(res.budget().status()).isEqualTo(BudgetStatus.NORMAL); }
}
