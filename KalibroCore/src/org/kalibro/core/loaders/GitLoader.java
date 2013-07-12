package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

/**
 * Loader for Git repositories.
 * 
 * @author Carlos Morais
 */
public class GitLoader extends RepositoryLoader {

	@Override
	public List<String> validationCommands() {
		return Arrays.asList("git --version");
	}

	@Override
	protected List<String> loadCommands(String address, boolean update) {
		if (update)
			return Arrays.asList("git pull");
		return Arrays.asList("git clone " + address + " .");
	}

	@Override
	protected String metadataDirectoryName() {
		return ".git";
	}
}