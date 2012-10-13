package org.kalibro.core.loaders;

import org.kalibro.RepositoryType;

public class MercurialTest extends RepositoryIntegrationTest {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.MERCURIAL;
	}
}