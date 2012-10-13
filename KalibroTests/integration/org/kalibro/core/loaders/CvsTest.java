package org.kalibro.core.loaders;

import org.kalibro.RepositoryType;

public class CvsTest extends RepositoryIntegrationTest {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.CVS;
	}
}