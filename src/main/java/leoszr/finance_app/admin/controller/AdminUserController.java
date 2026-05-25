package leoszr.finance_app.admin.controller;

import jakarta.validation.Valid;
import leoszr.finance_app.admin.dto.*;
import leoszr.finance_app.admin.service.AdminUserService;
import leoszr.finance_app.security.CurrentUserProvider;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {
	private final AdminUserService service; private final CurrentUserProvider currentUser;
	public AdminUserController(AdminUserService service, CurrentUserProvider currentUser) { this.service = service; this.currentUser = currentUser; }
	@GetMapping public Page<AdminUserResponse> list(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) { return service.list(pageable); }
	@GetMapping("/{id}") public AdminUserResponse get(@PathVariable UUID id) { return service.get(id); }
	@PatchMapping("/{id}/status") public AdminUserResponse updateStatus(@PathVariable UUID id, @Valid @RequestBody UpdateUserStatusRequest req) { return service.updateStatus(currentUser.currentUser().userId(), id, req); }
	@PostMapping("/{id}/sessions/revoke") @ResponseStatus(HttpStatus.NO_CONTENT) public void revokeSessions(@PathVariable UUID id) { service.revokeSessions(currentUser.currentUser().userId(), id); }
}
