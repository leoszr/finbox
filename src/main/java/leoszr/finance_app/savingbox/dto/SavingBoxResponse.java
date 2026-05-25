package leoszr.finance_app.savingbox.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record SavingBoxResponse(UUID id, String name, String currency, BigDecimal balance, BigDecimal targetAmount, boolean defaultBox) {}
