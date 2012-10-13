package org.kalibro.core.loaders;

import org.kalibro.RepositoryType;

public class SubversionTest extends RepositoryIntegrationTest {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.SUBVERSION;
	}
}