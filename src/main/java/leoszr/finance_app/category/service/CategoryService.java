package leoszr.finance_app.category.service;

import leoszr.finance_app.category.dto.*;
import leoszr.finance_app.category.entity.*;
import leoszr.finance_app.category.repository.CategoryRepository;
import leoszr.finance_app.common.*;
import leoszr.finance_app.user.entity.User;
import leoszr.finance_app.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CategoryService {
	private final CategoryRepository categories;
	private final UserRepository users;
	public CategoryService(CategoryRepository categories, UserRepository users) { this.categories = categories; this.users = users; }
	@Transactional public CategoryResponse create(UUID userId, CategoryRequest request) {
		User user = users.findById(userId).orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
		String normalized = TextNormalizer.normalize(request.name());
		if (categories.existsByUserIdAndNormalizedName(userId, normalized)) throw new BusinessRuleException("Categoria já existe.");
		Category c = new Category(); c.setUser(user); c.setName(request.name().trim()); c.setNormalizedName(normalized); c.setColor(request.color()); c.setType(request.type()); c.setSpecialType(CategorySpecialType.CUSTOM);
		return toResponse(categories.save(c));
	}
	@Transactional(readOnly = true) public List<CategoryResponse> list(UUID userId) { return categories.findByUserIdOrderBySpecialTypeAscNameAsc(userId).stream().map(this::toResponse).toList(); }
	@Transactional public CategoryResponse update(UUID userId, UUID id, CategoryRequest request) {
		Category c = categories.findByIdAndUserId(id, userId).orElseThrow(() -> new NotFoundException("Categoria não encontrada."));
		if (c.getSpecialType() != CategorySpecialType.CUSTOM) throw new BusinessRuleException("Categoria especial não pode ser editada.");
		String normalized = TextNormalizer.normalize(request.name());
		if (categories.existsByUserIdAndNormalizedNameAndIdNot(userId, normalized, id)) throw new BusinessRuleException("Categoria já existe.");
		c.setName(request.name().trim()); c.setNormalizedName(normalized); c.setColor(request.color()); c.setType(request.type());
		return toResponse(c);
	}
	@Transactional public void delete(UUID userId, UUID id) {
		Category c = categories.findByIdAndUserId(id, userId).orElseThrow(() -> new NotFoundException("Categoria não encontrada."));
		if (c.getSpecialType() != CategorySpecialType.CUSTOM) throw new BusinessRuleException("Categoria especial não pode ser excluída.");
		categories.delete(c);
	}
	private CategoryResponse toResponse(Category c) { return new CategoryResponse(c.getId(), c.getName(), c.getColor(), c.getType(), c.getSpecialType()); }
}
