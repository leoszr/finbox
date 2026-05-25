package leoszr.finance_app.common;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ApiExceptionHandlerTest {

	private final MockMvc mockMvc = MockMvcBuilders
			.standaloneSetup(new TestController())
			.setControllerAdvice(new ApiExceptionHandler())
			.build();

	@Test
	void validationErrorsReturnValidationError() throws Exception {
		mockMvc.perform(post("/test/validation")
					.contentType(MediaType.APPLICATION_JSON)
					.content("{}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.timestamp", not(blankOrNullString())))
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
				.andExpect(jsonPath("$.message", not(blankOrNullString())))
				.andExpect(jsonPath("$.path").value("/test/validation"));
	}

	@Test
	void notFoundErrorsReturnNotFound() throws Exception {
		mockMvc.perform(get("/test/not-found"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.error").value("NOT_FOUND"))
				.andExpect(jsonPath("$.message").value("Recurso não encontrado."))
				.andExpect(jsonPath("$.path").value("/test/not-found"));
	}

	@Test
	void businessRuleErrorsReturnBadRequest() throws Exception {
		mockMvc.perform(get("/test/business-rule"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.error").value("BUSINESS_RULE_ERROR"))
				.andExpect(jsonPath("$.message").value("Regra violada."))
				.andExpect(jsonPath("$.path").value("/test/business-rule"));
	}

	@Test
	void unauthorizedErrorsReturnUnauthorized() throws Exception {
		mockMvc.perform(get("/test/unauthorized"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.status").value(401))
				.andExpect(jsonPath("$.error").value("UNAUTHORIZED"))
				.andExpect(jsonPath("$.message").value("Token inválido."))
				.andExpect(jsonPath("$.path").value("/test/unauthorized"));
	}

	@RestController
	@RequestMapping("/test")
	static class TestController {

		@PostMapping("/validation")
		void validation(@Valid @RequestBody TestRequest request) {
		}

		@GetMapping("/not-found")
		void notFound() {
			throw new NotFoundException("Recurso não encontrado.");
		}

		@GetMapping("/business-rule")
		void businessRule() {
			throw new BusinessRuleException("Regra violada.");
		}

		@GetMapping("/unauthorized")
		void unauthorized() {
			throw new UnauthorizedException("Token inválido.");
		}
	}

	record TestRequest(@NotBlank String name) {
	}
}
