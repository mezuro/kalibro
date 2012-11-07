package org.kalibro.core.loaders;

public class LocalZipTest extends RepositoryIntegrationTest {

	@Override
	protected String address() {
		return repositoriesDirectory().getAbsolutePath() + "/HelloWorld.zip";
	}
}