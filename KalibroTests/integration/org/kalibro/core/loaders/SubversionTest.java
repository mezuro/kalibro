package org.kalibro.core.loaders;

public class SubversionTest extends RepositoryIntegrationTest {

	@Override
	protected String address() {
		return "file://" + repositoriesDirectory().getAbsolutePath() + "/HelloWorldSubversion";
	}
}