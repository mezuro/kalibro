package org.kalibro.core.loaders;

import org.kalibro.RepositoryType;

public class RemoteTarballLoaderTest extends RemoteFileLoaderTestCase {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.REMOTE_TARBALL;
	}

	@Override
	protected RepositoryLoader expectedLocalLoader() {
		return new LocalTarballLoader();
	}
}