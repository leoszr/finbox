package leoszr.finance_app.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import leoszr.finance_app.category.entity.CategoryType;

public record CategoryRequest(@NotBlank @Size(max = 80) String name, @NotBlank @Size(max = 20) String color, @NotNull CategoryType type) {}
