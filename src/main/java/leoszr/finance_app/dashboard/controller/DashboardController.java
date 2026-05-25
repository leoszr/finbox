package leoszr.finance_app.dashboard.controller;

import leoszr.finance_app.dashboard.dto.DashboardResponse;
import leoszr.finance_app.dashboard.service.DashboardService;
import leoszr.finance_app.security.CurrentUserProvider;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
	private final CurrentUserProvider currentUserProvider; private final DashboardService service;
	public DashboardController(CurrentUserProvider currentUserProvider, DashboardService service){this.currentUserProvider=currentUserProvider;this.service=service;}
	@GetMapping public DashboardResponse get(@RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate referenceDate){ return service.build(userId(), referenceDate); }
	private UUID userId(){ return currentUserProvider.currentUser().userId(); }
}
