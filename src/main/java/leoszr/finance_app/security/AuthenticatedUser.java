package leoszr.finance_app.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import leoszr.finance_app.user.entity.UserRole;

public record AuthenticatedUser(
		UUID userId,
		String externalAuthId,
		String email,
		String name,
		UserRole role
) implements UserDetails {
	@Override public Collection<? extends GrantedAuthority> getAuthorities() { return List.of(new SimpleGrantedAuthority("ROLE_" + role.name())); }
	@Override public String getPassword() { return ""; }
	@Override public String getUsername() { return email; }
}
