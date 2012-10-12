package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.Repository;

public class CvsLoader extends RepositoryLoader {

	@Override
	public List<String> validationCommands() {
		return Arrays.asList("cvs --version");
	}

	@Override
	public boolean supportsAuthentication() {
		return false;
	}

	@Override
	public List<String> loadCommands(Repository repository, boolean update) {
		if (update)
			return Arrays.asList("cvs update");
		return Arrays.asList("cvs -z3 -d " + repository.getAddress() + " checkout -d . -P .");
	}
}