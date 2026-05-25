package leoszr.finance_app.admin.dto;

import leoszr.finance_app.user.entity.UserRole;
import leoszr.finance_app.user.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record AdminUserResponse(
		UUID id,
		String email,
		String name,
		UserStatus status,
		UserRole role,
		boolean emailVerified,
		Instant createdAt,
		Instant updatedAt
) {}
