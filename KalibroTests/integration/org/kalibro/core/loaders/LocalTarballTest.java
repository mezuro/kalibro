package org.kalibro.core.loaders;

import org.kalibro.RepositoryType;

public class LocalTarballTest extends LoaderIntegrationTest {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.LOCAL_TARBALL;
	}
}