package leoszr.finance_app.admin.service;

import leoszr.finance_app.admin.dto.*;
import leoszr.finance_app.auth.repository.RefreshTokenRepository;
import leoszr.finance_app.common.BusinessRuleException;
import leoszr.finance_app.common.NotFoundException;
import leoszr.finance_app.user.entity.User;
import leoszr.finance_app.user.entity.UserStatus;
import leoszr.finance_app.user.repository.UserRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AdminUserService {
	private final UserRepository users; private final RefreshTokenRepository refreshTokens;
	public AdminUserService(UserRepository users, RefreshTokenRepository refreshTokens) { this.users = users; this.refreshTokens = refreshTokens; }
	@Transactional(readOnly = true) public Page<AdminUserResponse> list(Pageable pageable) { return users.findAll(pageable).map(this::toResponse); }
	@Transactional(readOnly = true) public AdminUserResponse get(UUID id) { return toResponse(find(id)); }
	@Transactional public AdminUserResponse updateStatus(UUID adminId, UUID userId, UpdateUserStatusRequest req) {
		if (adminId.equals(userId) && req.status() != UserStatus.ACTIVE) throw new BusinessRuleException("Admin não pode desativar a própria conta.");
		User user = find(userId); user.setStatus(req.status()); if (req.status() != UserStatus.ACTIVE) refreshTokens.revokeActiveByUserId(userId); return toResponse(user);
	}
	@Transactional public void revokeSessions(UUID adminId, UUID userId) {
		if (adminId.equals(userId)) throw new BusinessRuleException("Admin não pode revogar a própria sessão por este endpoint.");
		find(userId); refreshTokens.revokeActiveByUserId(userId);
	}
	private User find(UUID id){ return users.findById(id).orElseThrow(() -> new NotFoundException("Usuário não encontrado.")); }
	private AdminUserResponse toResponse(User u){ return new AdminUserResponse(u.getId(), u.getEmail(), u.getName(), u.getStatus(), u.getRole(), u.isEmailVerified(), u.getCreatedAt(), u.getUpdatedAt()); }
}
