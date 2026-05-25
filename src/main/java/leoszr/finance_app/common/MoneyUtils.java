package leoszr.finance_app.common;

import java.math.*;

public final class MoneyUtils {
	private MoneyUtils() {}
	public static BigDecimal requirePositive(BigDecimal value) {
		if (value == null) throw new BusinessRuleException("Valor é obrigatório.");
		if (value.compareTo(new BigDecimal("0.01")) < 0) throw new BusinessRuleException("Valor inválido.");
		if (value.scale() > 2) throw new BusinessRuleException("Valor deve ter no máximo 2 casas decimais.");
		return value.setScale(2, RoundingMode.UNNECESSARY);
	}
}
