package org.kalibro.core.loaders;

import org.kalibro.core.model.enums.RepositoryType;

public class RemoteZipLoaderTest extends RemoteFileLoaderTestCase {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.REMOTE_ZIP;
	}

	@Override
	protected String expectedExtractorValidationCommand() {
		return "unzip -v";
	}

	@Override
	protected String expectedExtractionCommand(String temporaryFilePath, String loadPath) {
		return "unzip " + temporaryFilePath + " -d " + loadPath;
	}
}