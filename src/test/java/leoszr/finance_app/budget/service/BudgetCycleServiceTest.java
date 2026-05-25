package leoszr.finance_app.budget.service;

import leoszr.finance_app.budget.entity.BudgetCycleType;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

class BudgetCycleServiceTest {
	BudgetCycleService service = new BudgetCycleService();
	@Test void weeklyCycle(){ var r=service.currentCycle(BudgetCycleType.WEEKLY, LocalDate.parse("2026-05-04"), LocalDate.parse("2026-05-07")); assertThat(r.startDate()).isEqualTo("2026-05-04"); assertThat(r.endDate()).isEqualTo("2026-05-10"); }
	@Test void biweeklyCycle(){ var r=service.currentCycle(BudgetCycleType.BIWEEKLY, LocalDate.parse("2026-05-01"), LocalDate.parse("2026-05-20")); assertThat(r.startDate()).isEqualTo("2026-05-15"); assertThat(r.endDate()).isEqualTo("2026-05-28"); }
	@Test void monthlyCycleClampsShortMonths(){ var r=service.currentCycle(BudgetCycleType.MONTHLY, LocalDate.parse("2026-01-31"), LocalDate.parse("2026-02-15")); assertThat(r.startDate()).isEqualTo("2026-01-31"); assertThat(r.endDate()).isEqualTo("2026-02-27"); }
}
