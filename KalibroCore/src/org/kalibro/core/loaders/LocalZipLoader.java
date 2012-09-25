package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.Repository;

public class LocalZipLoader extends ProjectLoader {

	@Override
	public List<String> getValidationCommands() {
		return Arrays.asList("unzip -v");
	}

	@Override
	public boolean supportsAuthentication() {
		return true;
	}

	@Override
	public List<String> getLoadCommands(Repository repository, boolean update) {
		String command = "unzip -u -o";
		if (repository.hasAuthentication())
			command += " -P " + repository.getPassword();
		command += " " + repository.getAddress() + " -d .";
		return Arrays.asList(command);
	}

}