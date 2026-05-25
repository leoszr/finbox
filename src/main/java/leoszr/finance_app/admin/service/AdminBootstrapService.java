package leoszr.finance_app.admin.service;

import leoszr.finance_app.user.entity.*;
import leoszr.finance_app.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AdminBootstrapService implements CommandLineRunner {
	private final UserRepository users; private final PasswordEncoder passwordEncoder;
	private final String email; private final String password; private final String name;
	public AdminBootstrapService(UserRepository users, PasswordEncoder passwordEncoder,
			@Value("${admin.email:}") String email,
			@Value("${admin.password:}") String password,
			@Value("${admin.name:Admin}") String name) {
		this.users=users; this.passwordEncoder=passwordEncoder; this.email=email; this.password=password; this.name=name;
	}
	@Override public void run(String... args) {
		if (!StringUtils.hasText(email) || !StringUtils.hasText(password)) return;
		User user = users.findByEmailIgnoreCase(email).orElseGet(User::new);
		user.setEmail(email.trim().toLowerCase()); user.setName(StringUtils.hasText(user.getName()) ? user.getName() : name);
		user.setPasswordHash(passwordEncoder.encode(password)); user.setEmailVerified(true); user.setStatus(UserStatus.ACTIVE); user.setRole(UserRole.ADMIN);
		if (!StringUtils.hasText(user.getFirebaseUid())) user.setFirebaseUid("admin:" + user.getEmail());
		if (user.getPreferences() == null) user.setPreferences(new UserPreferences());
		users.save(user);
	}
}
