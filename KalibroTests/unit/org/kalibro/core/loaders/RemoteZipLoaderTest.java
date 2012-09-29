package org.kalibro.core.loaders;

import org.kalibro.RepositoryType;

public class RemoteZipLoaderTest extends RemoteFileLoaderTestCase {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.REMOTE_ZIP;
	}

	@Override
	protected ProjectLoader expectedLocalLoader() {
		return new LocalZipLoader();
	}
}