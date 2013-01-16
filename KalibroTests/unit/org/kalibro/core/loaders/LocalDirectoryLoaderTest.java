package org.kalibro.core.loaders;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class LocalDirectoryLoaderTest extends RepositoryLoaderTestCase {

	@Override
	protected List<String> expectedValidationCommands() {
		return list("cp --version");
	}

	@Override
	protected List<String> expectedLoadCommands() {
		return list("cp -ru " + ADDRESS + " .");
	}

	@Override
	protected List<String> expectedUpdateCommands() {
		return list("cp -ru " + ADDRESS + " .");
	}

	@Test
	public void shouldNotBeUpdatable() {
		assertFalse(loader.isUpdatable(null));
	}

	@Override
	protected String expectedMetadataDirectoryName() {
		return null;
	}
}