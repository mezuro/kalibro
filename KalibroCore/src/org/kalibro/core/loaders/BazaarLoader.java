package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

/**
 * Loader for Bazaar repositories.
 * 
 * @author Carlos Morais
 */
public class BazaarLoader extends RepositoryLoader {

	@Override
	public List<String> validationCommands() {
		return Arrays.asList("bzr --version");
	}

	@Override
	public List<String> loadCommands(String address, boolean update) {
		if (update)
			return Arrays.asList("bzr pull --overwrite");
		return Arrays.asList("bzr branch --use-existing-dir " + address + " .");
	}
}