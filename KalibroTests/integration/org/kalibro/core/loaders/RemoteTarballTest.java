package org.kalibro.core.loaders;

import org.kalibro.RepositoryType;

public class RemoteTarballTest extends RemoteFileLoaderIntegrationTest {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.REMOTE_TARBALL;
	}
}