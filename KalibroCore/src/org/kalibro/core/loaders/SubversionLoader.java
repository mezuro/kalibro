package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

/**
 * Loader for Subversion repositories.
 * 
 * @author Carlos Morais
 */
public class SubversionLoader extends RepositoryLoader {

	@Override
	public List<String> validationCommands() {
		return Arrays.asList("svn --version");
	}

	@Override
	public List<String> loadCommands(String address, boolean update) {
		if (update)
			return Arrays.asList("svn update");
		return Arrays.asList("svn checkout " + address + " .");
	}
}