package org.kalibro.core.loaders;

import static org.junit.Assert.assertEquals;
import static org.kalibro.core.model.RepositoryFixtures.helloWorldRepository;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.model.Repository;
import org.kalibro.core.model.enums.RepositoryType;
import org.powermock.reflect.Whitebox;

public abstract class ProjectLoaderTestCase extends TestCase {

	protected ProjectLoader loader;
	protected Repository repository;

	@Before
	public void setUp() {
		RepositoryType repositoryType = getRepositoryType();
		loader = Whitebox.getInternalState(repositoryType, ProjectLoader.class);
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