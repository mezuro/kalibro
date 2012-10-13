package org.kalibro.core.loaders;

import org.kalibro.RepositoryType;

public class LocalDirectoryTest extends RepositoryIntegrationTest {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.LOCAL_DIRECTORY;
	}
}