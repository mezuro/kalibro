package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.Repository;

public class LocalTarballLoader extends RepositoryLoader {

	@Override
	public List<String> validationCommands() {
		return Arrays.asList("tar --version");
	}

	@Override
	public boolean supportsAuthentication() {
		return false;
	}

	@Override
	public List<String> loadCommands(Repository repository, boolean update) {
		return Arrays.asList("tar -x --keep-newer-files -f " + repository.getAddress() + " -C .");
	}
}