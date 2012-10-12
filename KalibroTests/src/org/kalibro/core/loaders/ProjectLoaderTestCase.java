package org.kalibro.core.loaders;

import static org.junit.Assert.assertEquals;
import static org.kalibro.core.loaders.RepositoryFixtures.helloWorldRepository;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.Repository;
import org.kalibro.RepositoryType;
import org.kalibro.tests.UnitTest;
import org.powermock.reflect.Whitebox;

public abstract class ProjectLoaderTestCase extends UnitTest {

	protected RepositoryLoader loader;
	protected Repository repository;

	@Before
	public void setUp() {
		RepositoryType repositoryType = getRepositoryType();
		loader = Whitebox.getInternalState(repositoryType, RepositoryLoader.class);
		repository = helloWorldRepository(repositoryType);
		if (loader.supportsAuthentication()) {
			repository.setUsername("USERNAME");
			repository.setPassword("PASSWORD");
		}
	}

	protected abstract RepositoryType getRepositoryType();

	@Test
	public void checkValidationCommands() {
		assertDeepEquals(expectedValidationCommands(), loader.getValidationCommands());
	}

	protected abstract List<String> expectedValidationCommands();

	@Test
	public void checkAuthenticationSupport() {
		assertEquals(shouldSupportAuthentication(), loader.supportsAuthentication());
	}

	protected abstract boolean shouldSupportAuthentication();

	@Test
	public void checkLoadCommands() {
		assertDeepEquals(expectedLoadCommands(true), loader.getLoadCommands(repository, true));
		assertDeepEquals(expectedLoadCommands(false), loader.getLoadCommands(repository, false));
	}

	protected abstract List<String> expectedLoadCommands(boolean update);
}