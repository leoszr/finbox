package leoszr.finance_app.user.repository;

import leoszr.finance_app.TestcontainersConfiguration;
import leoszr.finance_app.user.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@SpringBootTest
@Transactional
class UserRepositoryIT {
	@Autowired UserRepository users;
	@Test void savesUserAndPreferencesAndFindsByFirebaseUid() {
		User user = new User(); user.setFirebaseUid("repo-uid"); user.setEmail("repo@example.com"); user.setName("Repo"); user.setPreferences(new UserPreferences()); users.saveAndFlush(user);
		User found = users.findByExternalAuthId("repo-uid").orElseThrow();
		assertThat(found.getCreatedAt()).isNotNull();
		assertThat(found.getPreferences().getDefaultSort()).isEqualTo(DefaultSort.NEWEST_FIRST);
	}
}
