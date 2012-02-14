package org.kalibro.core.model;

import static org.junit.Assert.*;
import static org.kalibro.core.model.RepositoryFixtures.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.enums.RepositoryType;

public class RepositoryTest extends KalibroTestCase {

	private Repository repository;

	@Before
	public void setUp() {
		repository = new Repository();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testInitialization() {
		repository = new Repository();
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
	public void shouldGetLoadCommandsFromType() {
		for (RepositoryType type : RepositoryType.values())
			shouldGetLoadCommandsFrom(type);
	}

	private void shouldGetLoadCommandsFrom(RepositoryType type) {
		repository = helloWorldRepository(type);
		String helloWorldPath = HELLO_WORLD_DIRECTORY.getAbsolutePath();
		List<String> expected = type.getProjectLoader().getLoadCommands(repository, helloWorldPath);
		List<String> actual = repository.getLoadCommands(helloWorldPath);
		assertDeepEquals(expected, actual);
	}
}