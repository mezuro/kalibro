package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.core.model.Repository;

public class SubversionLoader implements ProjectLoader {

	@Override
	public List<String> getValidationCommands() {
		return Arrays.asList("svn --version");
	}

	@Override
	public boolean supportsAuthentication() {
		return true;
	}

	@Override
	public List<String> getLoadCommands(Repository repository, String loadPath) {
		String command = "svn checkout";
		if (repository.hasAuthentication())
			command += " --username " + repository.getUsername() + " --password " + repository.getPassword();
		command += " " + repository.getAddress() + " " + loadPath;
		return Arrays.asList(command);
	}
}