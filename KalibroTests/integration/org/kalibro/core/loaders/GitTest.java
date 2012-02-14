package org.kalibro.core.loaders;

import org.kalibro.core.model.enums.RepositoryType;

public class GitTest extends LoaderIntegrationTest {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.GIT;
	}
}