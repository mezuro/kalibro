package org.kalibro.core.loaders;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class LocalZipLoaderTest extends LoaderTestCase {

	@Override
	protected List<String> expectedValidationCommands() {
		return list("unzip -v");
	}

	@Override
	protected List<String> expectedLoadCommands() {
		return list("unzip -u -o " + ADDRESS + " -d .");
	}

	@Override
	protected List<String> expectedUpdateCommands() {
		return list("unzip -u -o " + ADDRESS + " -d .");
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