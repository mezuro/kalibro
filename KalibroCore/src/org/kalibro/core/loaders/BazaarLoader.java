package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.Repository;

public class BazaarLoader extends RepositoryLoader {

	@Override
	public List<String> getValidationCommands() {
		return Arrays.asList("bzr --version");
	}

	@Override
	public boolean supportsAuthentication() {
		return false;
	}

	@Override
	public List<String> getLoadCommands(Repository repository, boolean update) {
		if (update)
			return Arrays.asList("bzr pull --overwrite");
		return Arrays.asList("bzr branch --use-existing-dir " + repository.getAddress() + " .");
	}
}