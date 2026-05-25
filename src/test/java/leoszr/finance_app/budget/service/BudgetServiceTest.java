package leoszr.finance_app.budget.service;

import leoszr.finance_app.budget.dto.*;
import leoszr.finance_app.budget.entity.*;
import leoszr.finance_app.budget.repository.BudgetRepository;
import leoszr.finance_app.category.entity.Category;
import leoszr.finance_app.category.repository.CategoryRepository;
import leoszr.finance_app.common.BusinessRuleException;
import leoszr.finance_app.transaction.repository.TransactionRepository;
import leoszr.finance_app.user.entity.User;
import leoszr.finance_app.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class BudgetServiceTest {
	@Mock BudgetRepository budgets; @Mock CategoryRepository categories; @Mock UserRepository users; @Mock TransactionRepository transactions; @Mock BudgetCycleService cycles; @InjectMocks BudgetService service;
	UUID userId=UUID.randomUUID();
	@Test void createBudgetBlocksSecondActive(){ when(budgets.existsByUserIdAndActiveTrue(userId)).thenReturn(true); assertThatThrownBy(() -> service.create(userId, req("100.00"))).isInstanceOf(BusinessRuleException.class); }
	@Test void createBudgetValidatesCategorySum(){ UUID catId=UUID.randomUUID(); when(users.findById(userId)).thenReturn(Optional.of(new User())); when(categories.findByIdAndUserId(catId, userId)).thenReturn(Optional.of(new Category())); assertThatThrownBy(() -> service.create(userId, new BudgetRequest(new BigDecimal("10.00"), BudgetCycleType.WEEKLY, LocalDate.now(), List.of(new BudgetCategoryRequest(catId, new BigDecimal("11.00")))))).isInstanceOf(BusinessRuleException.class); }
	@Test void calculateUsageAndStatusWarning(){ Budget b=new Budget(); b.setTotalAmount(new BigDecimal("100.00")); b.setCycleType(BudgetCycleType.WEEKLY); b.setCycleStartDate(LocalDate.parse("2026-05-04")); when(budgets.findActiveByUserId(userId)).thenReturn(Optional.of(b)); when(cycles.currentCycle(eq(BudgetCycleType.WEEKLY), any(), any())).thenReturn(new BudgetCycleRange(LocalDate.parse("2026-05-04"), LocalDate.parse("2026-05-10"))); when(transactions.sumExpensesInPeriod(any(), any(), any())).thenReturn(new BigDecimal("80.00")); assertThat(service.currentCycle(userId, LocalDate.parse("2026-05-05")).status()).isEqualTo(BudgetStatus.WARNING); }
	private BudgetRequest req(String total){ return new BudgetRequest(new BigDecimal(total), BudgetCycleType.WEEKLY, LocalDate.now(), List.of()); }
}
