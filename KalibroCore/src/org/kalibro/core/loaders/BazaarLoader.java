package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.KalibroException;

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

	@Override
	protected String metadataDirectoryName() {
		return ".bzr";
	}

	@Override
	// FIXME
	protected List<String> rollBackOneCommit(boolean update) {
		if (!update)
			throw new KalibroException(LOAD_ERROR_MESSAGE); 
		return null;
	}
}