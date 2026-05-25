package leoszr.finance_app.budget.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record BudgetCategoryResponse(UUID categoryId, String categoryName, BigDecimal amount) {}
