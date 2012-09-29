package org.kalibro.core.loaders;

import org.kalibro.RepositoryType;

public class CvsTest extends LoaderIntegrationTest {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.CVS;
	}
}