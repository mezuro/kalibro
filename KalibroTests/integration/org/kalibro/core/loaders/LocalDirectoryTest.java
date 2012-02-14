package org.kalibro.core.loaders;

import org.kalibro.core.model.enums.RepositoryType;

public class LocalDirectoryTest extends LoaderIntegrationTest {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.LOCAL_DIRECTORY;
	}
}