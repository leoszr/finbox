package leoszr.finance_app.category.dto;

import leoszr.finance_app.category.entity.*;
import java.util.UUID;

public record CategoryResponse(UUID id, String name, String color, CategoryType type, CategorySpecialType specialType) {}
