package org.kalibro.core.loaders;

import org.kalibro.RepositoryType;

public class RemoteZipTest extends RemoteFileLoaderIntegrationTest {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.REMOTE_ZIP;
	}
}