package leoszr.finance_app.user.service;

import leoszr.finance_app.common.NotFoundException;
import leoszr.finance_app.user.dto.*;
import leoszr.finance_app.user.entity.*;
import leoszr.finance_app.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
public class UserService {
	private final UserRepository users;
	public UserService(UserRepository users) { this.users = users; }
	@Transactional(readOnly = true)
	public UserResponse getMe(UUID userId) { return toResponse(find(userId)); }
	@Transactional
	public UserResponse updateProfile(UUID userId, UpdateUserRequest request) {
		User user = find(userId);
		if (request.name() != null) user.setName(request.name());
		if (StringUtils.hasText(request.currency())) user.setCurrency(request.currency());
		if (request.theme() != null) user.setTheme(request.theme());
		if (request.faceIdEnabled() != null) user.setFaceIdEnabled(request.faceIdEnabled());
		return toResponse(user);
	}
	@Transactional
	public UserResponse updatePreferences(UUID userId, UpdatePreferencesRequest request) {
		User user = find(userId); UserPreferences p = user.getPreferences();
		if (request.defaultPeriod() != null) p.setDefaultPeriod(request.defaultPeriod());
		if (request.defaultSort() != null) p.setDefaultSort(request.defaultSort());
		return toResponse(user);
	}
	private User find(UUID id) { return users.findById(id).orElseThrow(() -> new NotFoundException("Usuário não encontrado.")); }
	private UserResponse toResponse(User u) {
		UserPreferences p = u.getPreferences();
		return new UserResponse(u.getId(), u.getFirebaseUid(), u.getEmail(), u.getName(), u.getCurrency(), u.getTheme(), u.isFaceIdEnabled(), new PreferencesResponse(p.getDefaultPeriod(), p.getDefaultSort()));
	}
}
