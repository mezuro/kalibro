package org.kalibro.core.loaders;

import org.kalibro.RepositoryType;

public class BazaarTest extends LoaderIntegrationTest {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.BAZAAR;
	}
}