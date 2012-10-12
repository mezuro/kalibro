package org.kalibro.core.loaders;

import java.util.ArrayList;
import java.util.List;

import org.kalibro.Repository;

public abstract class RemoteFileLoaderTestCase extends RepositoryLoaderTestCase {

	private static final String DOWNLOAD_PREFIX = "wget -N --user=USERNAME --password=PASSWORD ";

	@Override
	protected List<String> expectedValidationCommands() {
		List<String> expectedValidationCommands = new ArrayList<String>();
		expectedValidationCommands.add("wget --version");
		expectedValidationCommands.addAll(expectedLocalLoader().validationCommands());
		return expectedValidationCommands;
	}

	@Override
	protected boolean shouldSupportAuthentication() {
		return true;
	}

	@Override
	protected List<String> expectedLoadCommands(boolean update) {
		String temporaryFilePath = "./." + loader.hashCode();
		Repository localRepository = new Repository(null, temporaryFilePath);
		List<String> expectedLoadCommands = new ArrayList<String>();
		expectedLoadCommands.add(DOWNLOAD_PREFIX + repository.getAddress() + " -O " + temporaryFilePath);
		expectedLoadCommands.addAll(expectedLocalLoader().loadCommands(localRepository, update));
		return expectedLoadCommands;
	}

	protected abstract RepositoryLoader expectedLocalLoader();
}