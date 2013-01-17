package org.kalibro.core.loaders;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class LocalTarballLoaderTest extends LoaderTestCase {

	@Override
	protected List<String> expectedValidationCommands() {
		return list("tar --version");
	}

	@Override
	protected List<String> expectedLoadCommands() {
		return list("tar -x --keep-newer-files -f " + ADDRESS + " -C .");
	}

	@Override
	protected List<String> expectedUpdateCommands() {
		return list("tar -x --keep-newer-files -f " + ADDRESS + " -C .");
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