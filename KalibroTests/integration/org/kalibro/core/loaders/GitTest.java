package org.kalibro.core.loaders;

import org.kalibro.RepositoryType;

public class GitTest extends RepositoryIntegrationTest {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.GIT;
	}
}