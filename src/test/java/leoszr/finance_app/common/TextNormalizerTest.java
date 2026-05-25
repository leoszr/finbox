package leoszr.finance_app.common;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class TextNormalizerTest {
	@Test void normalizesAccentsCaseAndSpaces() { assertThat(TextNormalizer.normalize("  Alimentação   Extra ")).isEqualTo("alimentacao extra"); }
	@Test void blankIsInvalid() { assertThatThrownBy(() -> TextNormalizer.normalize("  ")).isInstanceOf(BusinessRuleException.class); }
}
