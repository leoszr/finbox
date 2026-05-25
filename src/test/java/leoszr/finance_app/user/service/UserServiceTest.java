package leoszr.finance_app.user.service;

import leoszr.finance_app.user.dto.*;
import leoszr.finance_app.user.entity.*;
import leoszr.finance_app.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserServiceTest {
	@Mock UserRepository users;
	@InjectMocks UserService service;
	UUID id;
	User user;
	@BeforeEach void setUp() { id = UUID.randomUUID(); user = new User(); user.setFirebaseUid("uid"); user.setEmail("old@e.com"); user.setName("Old"); user.setPreferences(new UserPreferences()); when(users.findById(id)).thenReturn(Optional.of(user)); }
	@Test void updateProfileDoesNotChangeFirebaseUidOrEmail() {
		UserResponse res = service.updateProfile(id, new UpdateUserRequest("New", "BRL", Theme.DARK, true));
		assertThat(res.name()).isEqualTo("New");
		assertThat(res.email()).isEqualTo("old@e.com");
		assertThat(res.externalAuthId()).isEqualTo("uid");
		assertThat(res.theme()).isEqualTo(Theme.DARK);
		assertThat(res.faceIdEnabled()).isTrue();
	}
	@Test void updatePreferencesChangesCurrentUserOnly() {
		UserResponse res = service.updatePreferences(id, new UpdatePreferencesRequest(DefaultPeriod.WEEKLY, DefaultSort.OLDEST_FIRST));
		assertThat(res.preferences().defaultPeriod()).isEqualTo(DefaultPeriod.WEEKLY);
		assertThat(res.preferences().defaultSort()).isEqualTo(DefaultSort.OLDEST_FIRST);
	}
}
