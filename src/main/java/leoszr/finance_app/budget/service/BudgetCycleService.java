package leoszr.finance_app.budget.service;

import leoszr.finance_app.budget.dto.BudgetCycleRange;
import leoszr.finance_app.budget.entity.BudgetCycleType;
import org.springframework.stereotype.Service;
import java.time.*;
import java.time.temporal.ChronoUnit;

@Service
public class BudgetCycleService {
	public BudgetCycleRange currentCycle(BudgetCycleType type, LocalDate anchor, LocalDate reference) {
		return switch (type) { case WEEKLY -> fixed(anchor, reference, 7); case BIWEEKLY -> fixed(anchor, reference, 14); case MONTHLY -> monthly(anchor, reference); };
	}
	private BudgetCycleRange fixed(LocalDate anchor, LocalDate ref, int days) { long diff=ChronoUnit.DAYS.between(anchor, ref); long n=Math.floorDiv(diff, days); LocalDate start=anchor.plusDays(n*days); return new BudgetCycleRange(start, start.plusDays(days-1)); }
	private BudgetCycleRange monthly(LocalDate anchor, LocalDate ref) { int day=anchor.getDayOfMonth(); LocalDate start=candidate(ref.getYear(), ref.getMonthValue(), day); if (ref.isBefore(start)) start=candidate(ref.minusMonths(1).getYear(), ref.minusMonths(1).getMonthValue(), day); LocalDate next=candidate(start.plusMonths(1).getYear(), start.plusMonths(1).getMonthValue(), day); return new BudgetCycleRange(start, next.minusDays(1)); }
	private LocalDate candidate(int year, int month, int day) { YearMonth ym=YearMonth.of(year, month); return ym.atDay(Math.min(day, ym.lengthOfMonth())); }
}
