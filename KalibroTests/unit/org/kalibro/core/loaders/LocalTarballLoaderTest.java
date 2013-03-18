package org.kalibro.core.loaders;

import java.util.List;

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
}