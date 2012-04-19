package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.core.model.Repository;

public class BazaarLoader extends ProjectLoader {

	@Override
	public List<String> getValidationCommands() {
		return Arrays.asList("bzr --version");
	}

	@Override
	public boolean supportsAuthentication() {
		return false;
	}

	@Override
	public List<String> getLoadCommands(Repository repository, String loadPath) {
		return Arrays.asList("bzr branch --use-existing-dir " + repository.getAddress() + " " + loadPath);
	}

	@Override
	public List<String> getUpdateCommands(Repository repository, String loadPath) {
		return Arrays.asList("bzr pull --overwrite");
	}
}