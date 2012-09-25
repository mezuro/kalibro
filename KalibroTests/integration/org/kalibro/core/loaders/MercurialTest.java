package org.kalibro.core.loaders;

import org.kalibro.RepositoryType;

public class MercurialTest extends LoaderIntegrationTest {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.MERCURIAL;
	}
}