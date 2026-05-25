package leoszr.finance_app.user.dto;

import leoszr.finance_app.user.entity.*;
import java.util.UUID;

public record UserResponse(UUID id, String externalAuthId, String email, String name, String currency, Theme theme, boolean faceIdEnabled, UserStatus status, UserRole role, PreferencesResponse preferences) {}
