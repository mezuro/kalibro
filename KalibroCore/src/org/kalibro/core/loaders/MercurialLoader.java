package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.core.model.Repository;

public class MercurialLoader extends ProjectLoader {

	@Override
	public List<String> getValidationCommands() {
		return Arrays.asList("hg --version");
	}

	@Override
	public boolean supportsAuthentication() {
		return false;
	}

	@Override
	public List<String> getLoadCommands(Repository repository, String loadPath) {
		return Arrays.asList("hg clone " + repository.getAddress() + " " + loadPath);
	}

	@Override
	public List<String> getUpdateCommands(Repository repository, String loadPath) {
		return Arrays.asList("hg pull -u");
	}
}