package org.kalibro.core.loaders;

public class BazaarTest extends RepositoryIntegrationTest {

	@Override
	protected String address() {
		return repositoriesDirectory().getAbsolutePath() + "/HelloWorldBazaar/";
	}
}