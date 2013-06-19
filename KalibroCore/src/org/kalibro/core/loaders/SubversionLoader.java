package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.KalibroException;

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

	@Override
	protected String metadataDirectoryName() {
		return ".svn";
	}

	@Override
	protected List<String> rollBackOneCommit(boolean update) {
		if (!update)
			throw new KalibroException(LOAD_ERROR_MESSAGE);

		// command svn info + 1
		Long previousRevision = new Long(1);
		return Arrays.asList("svn update -r " + previousRevision);
	}
	
}