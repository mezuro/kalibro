package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.core.model.Repository;

public class LocalDirectoryLoader implements ProjectLoader {

	@Override
	public List<String> getValidationCommands() {
		return Arrays.asList("cp --version");
	}

	@Override
	public boolean supportsAuthentication() {
		return false;
	}

	@Override
	public List<String> getLoadCommands(Repository repository, String loadPath) {
		return Arrays.asList("cp -r " + repository.getAddress() + " " + loadPath);
	}

	@Override
	public List<String> getUpdateCommands(Repository repository, String loadPath) {
		return Arrays.asList("cp -ur " + repository.getAddress() + " " + loadPath);
	}
}