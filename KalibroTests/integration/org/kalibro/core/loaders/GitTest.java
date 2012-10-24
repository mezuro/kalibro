package org.kalibro.core.loaders;

public class GitTest extends RepositoryIntegrationTest {

	@Override
	protected String address() {
		return repositoriesDirectory().getAbsolutePath() + "/HelloWorldGit/";
	}
}