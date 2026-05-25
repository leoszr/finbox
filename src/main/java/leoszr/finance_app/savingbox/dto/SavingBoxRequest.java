package leoszr.finance_app.savingbox.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record SavingBoxRequest(@NotBlank @Size(max=80) String name, @DecimalMin("0.01") @Digits(integer=12, fraction=2) BigDecimal targetAmount) {}
