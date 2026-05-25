package leoszr.finance_app.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import leoszr.finance_app.auth.dto.*;
import leoszr.finance_app.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private final AuthService auth;
	public AuthController(AuthService auth) { this.auth = auth; }
	@PostMapping("/register") @ResponseStatus(HttpStatus.CREATED)
	AuthResponse register(@Valid @RequestBody RegisterRequest req, HttpServletRequest http) { return auth.register(req, ip(http), http.getHeader("User-Agent")); }
	@PostMapping("/login")
	AuthResponse login(@Valid @RequestBody LoginRequest req, HttpServletRequest http) { return auth.login(req, ip(http), http.getHeader("User-Agent")); }
	@PostMapping("/refresh")
	AuthResponse refresh(@Valid @RequestBody RefreshRequest req, HttpServletRequest http) { return auth.refresh(req.refreshToken(), ip(http), http.getHeader("User-Agent")); }
	@PostMapping("/logout") @ResponseStatus(HttpStatus.NO_CONTENT)
	void logout(@Valid @RequestBody RefreshRequest req) { auth.logout(req.refreshToken()); }
	private String ip(HttpServletRequest r) { String f=r.getHeader("X-Forwarded-For"); return f == null ? r.getRemoteAddr() : f.split(",")[0].trim(); }
}
