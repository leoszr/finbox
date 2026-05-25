package leoszr.finance_app.user.service;

import leoszr.finance_app.category.repository.CategoryRepository;
import leoszr.finance_app.security.AuthToken;
import leoszr.finance_app.user.entity.*;
import leoszr.finance_app.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserBootstrapServiceTest {
	@Mock UserRepository users;
	@Mock CategoryRepository categories;
	@InjectMocks UserBootstrapService service;
	@Test void createsUserWithDefaults() {
		when(users.findByExternalAuthId("uid")).thenReturn(Optional.empty());
		when(users.save(any())).thenAnswer(inv -> inv.getArgument(0));
		User user = service.getOrCreate(new AuthToken("uid", "u@e.com", "User"));
		assertThat(user.getCurrency()).isEqualTo("BRL");
		assertThat(user.getTheme()).isEqualTo(Theme.SYSTEM);
		assertThat(user.isFaceIdEnabled()).isFalse();
		assertThat(user.getPreferences().getDefaultPeriod()).isEqualTo(DefaultPeriod.MONTHLY);
		verify(users).save(any());
		verify(categories, times(2)).save(any());
	}
	@Test void existingUserNotDuplicated() {
		User existing = new User(); existing.setFirebaseUid("uid");
		when(users.findByExternalAuthId("uid")).thenReturn(Optional.of(existing));
		assertThat(service.getOrCreate(new AuthToken("uid", "u@e.com", "User"))).isSameAs(existing);
		verify(users, never()).save(any());
	}
}
