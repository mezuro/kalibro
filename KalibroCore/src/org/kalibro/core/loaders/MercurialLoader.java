package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.Repository;

public class MercurialLoader extends RepositoryLoader {

	@Override
	public List<String> getValidationCommands() {
		return Arrays.asList("hg --version");
	}

	@Override
	public boolean supportsAuthentication() {
		return false;
	}

	@Override
	public List<String> getLoadCommands(Repository repository, boolean update) {
		if (update)
			return Arrays.asList("hg pull -u");
		return Arrays.asList("hg clone " + repository.getAddress() + " .");
	}
}