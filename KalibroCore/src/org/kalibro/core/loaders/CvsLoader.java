package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.core.model.Repository;

public class CvsLoader implements ProjectLoader {

	@Override
	public List<String> getValidationCommands() {
		return Arrays.asList("cvs --version");
	}

	@Override
	public boolean supportsAuthentication() {
		return false;
	}

	@Override
	public List<String> getLoadCommands(Repository repository, String loadPath) {
		return Arrays.asList("cvs -z3 -d " + repository.getAddress() + " checkout -d " + loadPath + " -P .");
	}

	@Override
	public List<String> getUpdateCommands(Repository repository, String loadPath) {
		return Arrays.asList("cvs update");
	}
}