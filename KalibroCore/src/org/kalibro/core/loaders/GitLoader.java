package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.core.model.Repository;

public class GitLoader implements ProjectLoader {

	@Override
	public List<String> getValidationCommands() {
		return Arrays.asList("git --version");
	}

	@Override
	public boolean supportsAuthentication() {
		return false;
	}

	@Override
	public List<String> getLoadCommands(Repository repository, String loadPath) {
		return Arrays.asList("git clone --depth=1 " + repository.getAddress() + " " + loadPath);
	}
}