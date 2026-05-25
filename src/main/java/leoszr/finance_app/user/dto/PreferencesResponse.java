package leoszr.finance_app.user.dto;

import leoszr.finance_app.user.entity.*;

public record PreferencesResponse(DefaultPeriod defaultPeriod, DefaultSort defaultSort) {}
