package leoszr.finance_app.common;

import java.time.Instant;

public record ApiErrorResponse(
		Instant timestamp,
		int status,
		ApiErrorCode error,
		String message,
		String path
) {
}
