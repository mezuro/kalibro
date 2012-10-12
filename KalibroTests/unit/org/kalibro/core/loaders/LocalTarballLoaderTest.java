package org.kalibro.core.loaders;

import java.util.List;

public class LocalTarballLoaderTest extends RepositoryLoaderTestCase {

	@Override
	protected List<String> expectedValidationCommands() {
		return asList("tar --version");
	}

	@Override
	protected List<String> expectedLoadCommands() {
		return asList("tar -x --keep-newer-files -f " + ADDRESS + " -C .");
	}

	@Override
	protected List<String> expectedUpdateCommands() {
		return asList("tar -x --keep-newer-files -f " + ADDRESS + " -C .");
	}
}