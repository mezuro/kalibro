package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.Repository;

public class LocalZipLoader extends RepositoryLoader {

	@Override
	public List<String> validationCommands() {
		return Arrays.asList("unzip -v");
	}

	@Override
	public boolean supportsAuthentication() {
		return true;
	}

	@Override
	public List<String> loadCommands(Repository repository, boolean update) {
		String command = "unzip -u -o";
		if (repository.hasAuthentication())
			command += " -P " + repository.getPassword();
		command += " " + repository.getAddress() + " -d .";
		return Arrays.asList(command);
	}

}