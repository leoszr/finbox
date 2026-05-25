package leoszr.finance_app.user.service;

import leoszr.finance_app.category.entity.Category;
import leoszr.finance_app.category.entity.CategorySpecialType;
import leoszr.finance_app.category.entity.CategoryType;
import leoszr.finance_app.category.repository.CategoryRepository;
import leoszr.finance_app.common.TextNormalizer;
import leoszr.finance_app.security.AuthToken;
import leoszr.finance_app.user.entity.User;
import leoszr.finance_app.user.entity.UserPreferences;
import leoszr.finance_app.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class UserBootstrapService {
	private final UserRepository users;
	private final CategoryRepository categories;
	public UserBootstrapService(UserRepository users, CategoryRepository categories) { this.users = users; this.categories = categories; }
	@Transactional
	public User getOrCreate(AuthToken token) {
		return users.findByExternalAuthId(token.externalAuthId()).orElseGet(() -> create(token));
	}
	private User create(AuthToken token) {
		User user = new User();
		user.setFirebaseUid(token.externalAuthId());
		user.setEmail(token.email());
		user.setName(StringUtils.hasText(token.name()) ? token.name() : token.email());
		user.setPreferences(new UserPreferences());
		User saved = users.save(user);
		createSpecial(saved, "Não categorizado", "#9CA3AF", CategoryType.BOTH, CategorySpecialType.UNCATEGORIZED);
		createSpecial(saved, "Guardado", "#22C55E", CategoryType.EXPENSE, CategorySpecialType.SAVED);
		return saved;
	}
	private void createSpecial(User user, String name, String color, CategoryType type, CategorySpecialType specialType) {
		Category category = new Category();
		category.setUser(user); category.setName(name); category.setNormalizedName(TextNormalizer.normalize(name));
		category.setColor(color); category.setType(type); category.setSpecialType(specialType);
		categories.save(category);
	}
}
