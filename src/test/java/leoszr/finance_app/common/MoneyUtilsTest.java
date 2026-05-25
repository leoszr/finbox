package leoszr.finance_app.common;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.*;

class MoneyUtilsTest {
	@Test void acceptsPositiveScaleTwo(){ assertThat(MoneyUtils.requirePositive(new BigDecimal("1.20"))).isEqualByComparingTo("1.20"); }
	@Test void rejectsZeroNegativeAndScaleAboveTwo(){ assertThatThrownBy(() -> MoneyUtils.requirePositive(BigDecimal.ZERO)).isInstanceOf(BusinessRuleException.class); assertThatThrownBy(() -> MoneyUtils.requirePositive(new BigDecimal("-1.00"))).isInstanceOf(BusinessRuleException.class); assertThatThrownBy(() -> MoneyUtils.requirePositive(new BigDecimal("1.234"))).isInstanceOf(BusinessRuleException.class); }
}
