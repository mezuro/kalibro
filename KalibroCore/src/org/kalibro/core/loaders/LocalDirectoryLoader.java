package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.Repository;

public class LocalDirectoryLoader extends RepositoryLoader {

	@Override
	public List<String> validationCommands() {
		return Arrays.asList("cp --version");
	}

	@Override
	public boolean supportsAuthentication() {
		return false;
	}

	@Override
	public List<String> loadCommands(Repository repository, boolean update) {
		return Arrays.asList("cp -ru " + repository.getAddress() + " .");
	}
}