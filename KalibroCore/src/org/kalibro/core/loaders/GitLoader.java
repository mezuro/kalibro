package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.Repository;

public class GitLoader extends RepositoryLoader {

	@Override
	public List<String> getValidationCommands() {
		return Arrays.asList("git --version");
	}

	@Override
	public boolean supportsAuthentication() {
		return false;
	}

	@Override
	public List<String> getLoadCommands(Repository repository, boolean update) {
		if (update)
			return Arrays.asList("git pull origin master");
		return Arrays.asList("git clone " + repository.getAddress() + " .");
	}
}