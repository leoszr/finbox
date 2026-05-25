package leoszr.finance_app.budget.controller;

import jakarta.validation.Valid;
import leoszr.finance_app.budget.dto.*;
import leoszr.finance_app.budget.service.BudgetService;
import leoszr.finance_app.security.CurrentUserProvider;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/budget")
public class BudgetController {
	private final CurrentUserProvider currentUserProvider; private final BudgetService service;
	public BudgetController(CurrentUserProvider currentUserProvider, BudgetService service){this.currentUserProvider=currentUserProvider;this.service=service;}
	@GetMapping public BudgetResponse get(){ return service.getActive(userId()); }
	@PostMapping @ResponseStatus(HttpStatus.CREATED) public BudgetResponse create(@Valid @RequestBody BudgetRequest req){ return service.create(userId(), req); }
	@PatchMapping public BudgetResponse update(@Valid @RequestBody BudgetRequest req){ return service.update(userId(), req); }
	@GetMapping("/current-cycle") public BudgetUsageResponse currentCycle(@RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate referenceDate){ return service.currentCycle(userId(), referenceDate); }
	private UUID userId(){ return currentUserProvider.currentUser().userId(); }
}
