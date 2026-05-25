package leoszr.finance_app.category.service;

import leoszr.finance_app.category.dto.CategoryRequest;
import leoszr.finance_app.category.entity.*;
import leoszr.finance_app.category.repository.CategoryRepository;
import leoszr.finance_app.common.*;
import leoszr.finance_app.user.entity.User;
import leoszr.finance_app.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class CategoryServiceTest {
	@Mock CategoryRepository categories; @Mock UserRepository users; @InjectMocks CategoryService service;
	UUID userId = UUID.randomUUID(); User user = new User();
	@Test void createCategoryUsesCustomAndNormalizedUniqueName() {
		when(users.findById(userId)).thenReturn(Optional.of(user));
		when(categories.save(any())).thenAnswer(inv -> inv.getArgument(0));
		var res = service.create(userId, new CategoryRequest(" Alimentação ", "#fff", CategoryType.EXPENSE));
		assertThat(res.name()).isEqualTo("Alimentação"); assertThat(res.specialType()).isEqualTo(CategorySpecialType.CUSTOM);
		verify(categories).existsByUserIdAndNormalizedName(userId, "alimentacao");
	}
	@Test void duplicateNameIsBlocked() {
		when(users.findById(userId)).thenReturn(Optional.of(user));
		when(categories.existsByUserIdAndNormalizedName(userId, "alimentacao")).thenReturn(true);
		assertThatThrownBy(() -> service.create(userId, new CategoryRequest("alimentação", "#fff", CategoryType.EXPENSE))).isInstanceOf(BusinessRuleException.class);
	}
	@Test void updateBlocksSpecialCategory() {
		UUID id = UUID.randomUUID(); Category c = new Category(); c.setSpecialType(CategorySpecialType.SAVED);
		when(categories.findByIdAndUserId(id, userId)).thenReturn(Optional.of(c));
		assertThatThrownBy(() -> service.update(userId, id, new CategoryRequest("X", "#000", CategoryType.BOTH))).isInstanceOf(BusinessRuleException.class);
	}
	@Test void deleteUsesUserScopedLookup() {
		UUID id = UUID.randomUUID(); Category c = new Category(); c.setSpecialType(CategorySpecialType.CUSTOM);
		when(categories.findByIdAndUserId(id, userId)).thenReturn(Optional.of(c));
		service.delete(userId, id);
		verify(categories).delete(c);
	}
}
