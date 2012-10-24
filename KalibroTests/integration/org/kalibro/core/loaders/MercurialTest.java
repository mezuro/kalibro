package org.kalibro.core.loaders;

public class MercurialTest extends RepositoryIntegrationTest {

	@Override
	protected String address() {
		return repositoriesDirectory().getAbsolutePath() + "/HelloWorldMercurial/";
	}
}