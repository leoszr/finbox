package leoszr.finance_app.user.dto;

import jakarta.validation.constraints.Size;
import leoszr.finance_app.user.entity.Theme;

public record UpdateUserRequest(@Size(max = 120) String name, String currency, Theme theme, Boolean faceIdEnabled) {}
