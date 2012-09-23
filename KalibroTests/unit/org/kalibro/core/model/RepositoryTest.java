package org.kalibro.core.model;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.model.enums.RepositoryType;
import org.kalibro.tests.UnitTest;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RepositoryType.class)
public class RepositoryTest extends UnitTest {

	private Repository repository = new Repository();

	@Test
	public void testInitialization() {
		assertEquals(RepositoryType.LOCAL_DIRECTORY, repository.getType());
		assertEquals("", repository.getAddress());
		assertEquals("", repository.getUsername());
		assertEquals("", repository.getPassword());
	}

	@Test
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

	@Test
	public void shouldLoad() {
		File loadDirectory = mock(File.class);
		RepositoryType type = PowerMockito.mock(RepositoryType.class);
		repository.setType(type);
		repository.load(loadDirectory);
		Mockito.verify(type).load(repository, loadDirectory);
	}
}