package leoszr.finance_app.user.dto;

import leoszr.finance_app.user.entity.DefaultPeriod;
import leoszr.finance_app.user.entity.DefaultSort;

public record UpdatePreferencesRequest(DefaultPeriod defaultPeriod, DefaultSort defaultSort) {}
