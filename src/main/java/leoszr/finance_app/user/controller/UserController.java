package leoszr.finance_app.user.controller;

import jakarta.validation.Valid;
import leoszr.finance_app.security.CurrentUserProvider;
import leoszr.finance_app.user.dto.*;
import leoszr.finance_app.user.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/me")
public class UserController {
	private final CurrentUserProvider currentUserProvider;
	private final UserService userService;
	public UserController(CurrentUserProvider currentUserProvider, UserService userService) { this.currentUserProvider = currentUserProvider; this.userService = userService; }
	@GetMapping
	public UserResponse getMe() { return userService.getMe(currentUserProvider.currentUser().userId()); }
	@PatchMapping
	public UserResponse patchMe(@Valid @RequestBody UpdateUserRequest request) { return userService.updateProfile(currentUserProvider.currentUser().userId(), request); }
	@PatchMapping("/preferences")
	public UserResponse patchPreferences(@Valid @RequestBody UpdatePreferencesRequest request) { return userService.updatePreferences(currentUserProvider.currentUser().userId(), request); }
}
