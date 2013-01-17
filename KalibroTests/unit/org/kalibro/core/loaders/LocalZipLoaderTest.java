package org.kalibro.core.loaders;

import java.util.List;

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
}