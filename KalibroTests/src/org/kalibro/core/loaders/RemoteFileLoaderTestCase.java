package org.kalibro.core.loaders;

import java.util.ArrayList;
import java.util.List;

import org.kalibro.core.model.Repository;

public abstract class RemoteFileLoaderTestCase extends ProjectLoaderTestCase {

	@Override
	protected List<String> expectedValidationCommands() {
		List<String> expectedValidationCommands = new ArrayList<String>();
		expectedValidationCommands.add("wget --version");
		expectedValidationCommands.addAll(expectedLocalLoader().getValidationCommands());
		return expectedValidationCommands;
	}

	@Override
	protected boolean shouldSupportAuthentication() {
		return true;
	}

	@Override
	protected List<String> expectedLoadCommands(boolean update) {
		List<String> expectedLoadCommands = new ArrayList<String>();
		expectedLoadCommands.add("wget --user=USERNAME --password=PASSWORD " + repository.getAddress() + " -O ./TEMP");
		expectedLoadCommands.addAll(expectedLocalLoader().getLoadCommands(new Repository(null, "./TEMP"), update));
		expectedLoadCommands.add("rm ./TEMP");
		return expectedLoadCommands;
	}

	protected abstract ProjectLoader expectedLocalLoader();
}