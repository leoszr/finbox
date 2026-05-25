package leoszr.finance_app.common;

import org.springframework.util.StringUtils;

import java.text.Normalizer;
import java.util.Locale;

public final class TextNormalizer {
	private TextNormalizer() {}
	public static String normalize(String value) {
		if (!StringUtils.hasText(value)) throw new BusinessRuleException("Nome obrigatório.");
		String trimmed = value.trim().replaceAll("\\s+", " ");
		if (!StringUtils.hasText(trimmed)) throw new BusinessRuleException("Nome obrigatório.");
		return Normalizer.normalize(trimmed, Normalizer.Form.NFD)
				.replaceAll("\\p{M}", "")
				.toLowerCase(Locale.ROOT);
	}
}
