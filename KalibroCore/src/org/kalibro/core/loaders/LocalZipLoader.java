package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.core.model.Repository;

public class LocalZipLoader implements ProjectLoader {

	@Override
	public List<String> getValidationCommands() {
		return Arrays.asList("unzip -v");
	}

	@Override
	public boolean supportsAuthentication() {
		return true;
	}

	@Override
	public List<String> getLoadCommands(Repository repository, String loadPath) {
		String command = "unzip";
		if (repository.hasAuthentication())
			command += " -P " + repository.getPassword();
		command += " " + repository.getAddress() + " -d " + loadPath;
		return Arrays.asList(command);
	}
}