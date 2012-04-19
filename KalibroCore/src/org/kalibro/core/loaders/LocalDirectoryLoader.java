package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.core.model.Repository;

public class LocalDirectoryLoader extends ProjectLoader {

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
		return Arrays.asList("cp -ur " + repository.getAddress() + " " + loadPath);
	}
}