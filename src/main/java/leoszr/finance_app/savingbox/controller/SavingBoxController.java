package leoszr.finance_app.savingbox.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import leoszr.finance_app.savingbox.dto.*;
import leoszr.finance_app.savingbox.service.SavingBoxService;
import leoszr.finance_app.security.CurrentUserProvider;
import leoszr.finance_app.transaction.dto.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Validated
@RestController
@RequestMapping("/boxes")
public class SavingBoxController {
	private final CurrentUserProvider currentUserProvider; private final SavingBoxService service;
	public SavingBoxController(CurrentUserProvider currentUserProvider, SavingBoxService service){this.currentUserProvider=currentUserProvider;this.service=service;}
	@GetMapping public List<SavingBoxResponse> list(){ service.ensureDefault(userId()); return service.list(userId()); }
	@PostMapping @ResponseStatus(HttpStatus.CREATED) public SavingBoxResponse create(@Valid @RequestBody SavingBoxRequest req){ return service.create(userId(),req); }
	@GetMapping("/{id}") public SavingBoxResponse get(@PathVariable UUID id){ return service.get(userId(),id); }
	@PatchMapping("/{id}") public SavingBoxResponse update(@PathVariable UUID id, @Valid @RequestBody SavingBoxRequest req){ return service.update(userId(),id,req); }
	@DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable UUID id){ service.delete(userId(),id); }
	@GetMapping("/{id}/movements") public Page<TransactionResponse> movements(@PathVariable UUID id, @RequestParam(defaultValue="0") @Min(0) int page, @RequestParam(defaultValue="20") @Min(1) @Max(100) int size){ return service.movements(userId(),id,page,size); }
	private UUID userId(){ return currentUserProvider.currentUser().userId(); }
}
