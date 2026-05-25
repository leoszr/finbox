package leoszr.finance_app.admin.dto;

import jakarta.validation.constraints.NotNull;
import leoszr.finance_app.user.entity.UserStatus;

public record UpdateUserStatusRequest(@NotNull UserStatus status) {}
