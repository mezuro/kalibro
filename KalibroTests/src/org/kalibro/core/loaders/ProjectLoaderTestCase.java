package org.kalibro.core.loaders;

import static org.junit.Assert.*;
import static org.kalibro.core.model.RepositoryFixtures.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Repository;
import org.kalibro.core.model.enums.RepositoryType;

public abstract class ProjectLoaderTestCase extends KalibroTestCase {

	protected ProjectLoader loader;
	protected Repository repository;

	@Before
	public void setUp() {
		loader = getRepositoryType().getProjectLoader();
		repository = helloWorldRepository(getRepositoryType());
		if (loader.supportsAuthentication()) {
			repository.setUsername("USERNAME");
			repository.setPassword("PASSWORD");
		}
	}

	protected abstract RepositoryType getRepositoryType();

	@Test(timeout = UNIT_TIMEOUT)
	public void checkValidationCommands() {
		assertDeepEquals(expectedValidationCommands(), loader.getValidationCommands());
	}

	protected abstract List<String> expectedValidationCommands();

	@Test(timeout = UNIT_TIMEOUT)
	public void checkAuthenticationSupport() {
		assertEquals(shouldSupportAuthentication(), loader.supportsAuthentication());
	}

	protected abstract boolean shouldSupportAuthentication();

	@Test(timeout = UNIT_TIMEOUT)
	public void checkLoadCommands() {
		String loadPath = HELLO_WORLD_DIRECTORY.getAbsolutePath();
		assertDeepEquals(expectedLoadCommands(loadPath), loader.getLoadCommands(repository, loadPath));
	}

	protected abstract List<String> expectedLoadCommands(String loadPath);
}