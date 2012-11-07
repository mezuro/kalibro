package org.kalibro.core.loaders;

public class LocalDirectoryTest extends RepositoryIntegrationTest {

	@Override
	protected String address() {
		return repositoriesDirectory().getAbsolutePath() + "/HelloWorldDirectory/";
	}
}