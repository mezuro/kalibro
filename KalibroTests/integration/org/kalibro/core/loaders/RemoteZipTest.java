package org.kalibro.core.loaders;

import org.kalibro.core.model.enums.RepositoryType;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest(RemoteZipLoader.class)
public class RemoteZipTest extends RemoteFileTest {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.REMOTE_ZIP;
	}
}