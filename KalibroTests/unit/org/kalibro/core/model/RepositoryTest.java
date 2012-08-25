package org.kalibro.core.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.model.enums.RepositoryType;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RepositoryType.class)
public class RepositoryTest extends TestCase {

	private Repository repository = new Repository();

	@Test(timeout = UNIT_TIMEOUT)
	public void testInitialization() {
		assertEquals(RepositoryType.LOCAL_DIRECTORY, repository.getType());
		assertEquals("", repository.getAddress());
		assertEquals("", repository.getUsername());
		assertEquals("", repository.getPassword());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHaveAuthenticationIfHasUsernameOrPassword() {
		assertFalse(repository.hasAuthentication());

		repository.setUsername("USERNAME");
		repository.setPassword("");
		assertTrue(repository.hasAuthentication());

		repository.setUsername("");
		repository.setPassword("PASSWORD");
		assertTrue(repository.hasAuthentication());

		repository.setUsername("USERNAME");
		repository.setPassword("PASSWORD");
		assertTrue(repository.hasAuthentication());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldLoad() {
		RepositoryType type = PowerMockito.mock(RepositoryType.class);
		repository.setType(type);
		repository.load(helloWorldDirectory());
		Mockito.verify(type).load(repository, helloWorldDirectory());
	}
}