package org.kalibro.core.loaders;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public abstract class LoaderTestCase extends UnitTest {

	protected static final String ADDRESS = "RepositoryLoaderTestCase address";

	protected Loader loader;

	@Before
	public void setUp() throws Exception {
		Class<?> loaderClass = Class.forName(getClass().getName().replace("Test", ""));
		loader = (Loader) loaderClass.getConstructor().newInstance();
	}

	@Test
	public void checkValidationCommands() {
		assertDeepEquals(expectedValidationCommands(), loader.validationCommands());
	}

	protected abstract List<String> expectedValidationCommands();

	@Test
	public void checkLoadCommands() {
		assertDeepEquals(expectedLoadCommands(), loader.loadCommands(ADDRESS, false));
	}

	protected abstract List<String> expectedLoadCommands();

	@Test
	public void checkUpdateCommands() {
		assertDeepEquals(expectedUpdateCommands(), loader.loadCommands(ADDRESS, true));
	}

	protected abstract List<String> expectedUpdateCommands();

	@Test
	public void checkMetadataDirectoryName() {
		assertEquals(expectedMetadataDirectoryName(), loader.metadataDirectoryName());
	}

	protected abstract String expectedMetadataDirectoryName();
}