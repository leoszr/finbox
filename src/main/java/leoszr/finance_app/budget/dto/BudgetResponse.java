package leoszr.finance_app.budget.dto;

import leoszr.finance_app.budget.entity.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public record BudgetResponse(UUID id, BigDecimal totalAmount, BudgetCycleType cycleType, LocalDate cycleStartDate, boolean active, List<BudgetCategoryResponse> categories) {}
