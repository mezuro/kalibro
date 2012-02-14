package org.kalibro.core.loaders;

import org.kalibro.core.model.enums.RepositoryType;

public class SubversionTest extends LoaderIntegrationTest {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.SUBVERSION;
	}
}