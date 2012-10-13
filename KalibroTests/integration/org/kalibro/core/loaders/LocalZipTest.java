package org.kalibro.core.loaders;

import org.kalibro.RepositoryType;

public class LocalZipTest extends RepositoryIntegrationTest {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.LOCAL_ZIP;
	}
}