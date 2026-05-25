package leoszr.finance_app.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import leoszr.finance_app.common.UnauthorizedException;
import leoszr.finance_app.user.entity.User;
import leoszr.finance_app.user.service.UserBootstrapService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class BearerAuthenticationFilter extends OncePerRequestFilter {
	private final AuthTokenService authTokenService;
	private final UserBootstrapService userBootstrapService;
	public BearerAuthenticationFilter(AuthTokenService authTokenService, UserBootstrapService userBootstrapService) { this.authTokenService = authTokenService; this.userBootstrapService = userBootstrapService; }
	@Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (header != null && header.startsWith("Bearer ")) {
			try {
				AuthToken token = authTokenService.verify(header.substring(7));
				User user = userBootstrapService.getOrCreate(token);
				AuthenticatedUser principal = new AuthenticatedUser(user.getId(), user.getFirebaseUid(), user.getEmail(), user.getName());
				SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities()));
			} catch (UnauthorizedException ex) {
				SecurityContextHolder.clearContext();
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
				return;
			} catch (Exception ex) {
				SecurityContextHolder.clearContext();
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido.");
				return;
			}
		}
		filterChain.doFilter(request, response);
	}
}
