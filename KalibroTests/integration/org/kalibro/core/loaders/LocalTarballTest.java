package org.kalibro.core.loaders;

public class LocalTarballTest extends RepositoryIntegrationTest {

	@Override
	protected String address() {
		return repositoriesDirectory().getAbsolutePath() + "/HelloWorld.tar.gz";
	}
}