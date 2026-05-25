package leoszr.finance_app.transaction.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import leoszr.finance_app.security.CurrentUserProvider;
import leoszr.finance_app.transaction.dto.*;
import leoszr.finance_app.transaction.entity.*;
import leoszr.finance_app.transaction.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/transactions")
public class TransactionController {
	private final CurrentUserProvider currentUserProvider; private final TransactionService service;
	public TransactionController(CurrentUserProvider currentUserProvider, TransactionService service){this.currentUserProvider=currentUserProvider;this.service=service;}
	@PostMapping @ResponseStatus(HttpStatus.CREATED) public TransactionResponse create(@Valid @RequestBody TransactionRequest req){ return service.create(userId(), req); }
	@GetMapping("/{id}") public TransactionResponse get(@PathVariable UUID id){ return service.get(userId(), id); }
	@PatchMapping("/{id}") public TransactionResponse update(@PathVariable UUID id, @Valid @RequestBody TransactionRequest req){ return service.update(userId(), id, req); }
	@DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable UUID id){ service.delete(userId(), id); }
	@GetMapping public Page<TransactionResponse> list(@RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate, @RequestParam(required=false) UUID categoryId, @RequestParam(required=false) TransactionType type, @RequestParam(required=false) @DecimalMin("0.01") BigDecimal minAmount, @RequestParam(required=false) @DecimalMin("0.01") BigDecimal maxAmount, @RequestParam(required=false) String description, @RequestParam(required=false) HistorySortType sort, @RequestParam(defaultValue="0") @Min(0) int page, @RequestParam(defaultValue="20") @Min(1) @Max(100) int size){ return service.list(userId(), startDate, endDate, categoryId, type, minAmount, maxAmount, description, sort, page, size); }
	private UUID userId(){ return currentUserProvider.currentUser().userId(); }
}
