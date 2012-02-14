package org.kalibro.core.loaders;

import org.kalibro.core.model.enums.RepositoryType;

public class RemoteTarballLoaderTest extends RemoteFileLoaderTestCase {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.REMOTE_TARBALL;
	}

	@Override
	protected String expectedExtractorValidationCommand() {
		return "tar --version";
	}

	@Override
	protected String expectedExtractionCommand(String temporaryFilePath, String loadPath) {
		return "tar -xf " + temporaryFilePath + " -C " + loadPath;
	}
}