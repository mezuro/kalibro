package org.kalibro.core.loaders;

public class CvsTest extends RepositoryIntegrationTest {

	@Override
	protected String address() {
		return repositoriesDirectory().getAbsolutePath() + "/HelloWorldCvs/";
	}
}