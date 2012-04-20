package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.core.model.Repository;

public class SubversionLoader extends ProjectLoader {

	@Override
	public List<String> getValidationCommands() {
		return Arrays.asList("svn --version");
	}

	@Override
	public boolean supportsAuthentication() {
		return true;
	}

	@Override
	public List<String> getLoadCommands(Repository repository, boolean update) {
		if (update)
			return Arrays.asList("svn update" + authentication(repository));
		return Arrays.asList("svn checkout" + authentication(repository) + " " + repository.getAddress() + " .");
	}

	private String authentication(Repository repository) {
		String authentication = "";
		if (repository.hasAuthentication())
			authentication += " --username " + repository.getUsername() + " --password " + repository.getPassword();
		return authentication;
	}
}