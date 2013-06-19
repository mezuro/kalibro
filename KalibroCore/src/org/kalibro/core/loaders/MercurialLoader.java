package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.KalibroException;

/**
 * Loader for Mercurial repositories.
 * 
 * @author Carlos Morais
 */
public class MercurialLoader extends RepositoryLoader {

	@Override
	public List<String> validationCommands() {
		return Arrays.asList("hg --version");
	}

	@Override
	public List<String> loadCommands(String address, boolean update) {
		if (update)
			return Arrays.asList("hg pull -u");
		return Arrays.asList("hg clone " + address + " .");
	}

	@Override
	protected String metadataDirectoryName() {
		return ".hg";
	}

	@Override
	// FIXME
	protected List<String> rollBackOneCommit(boolean update) {
		if (!update)
			throw new KalibroException(LOAD_ERROR_MESSAGE); 
		return null;
	}
}