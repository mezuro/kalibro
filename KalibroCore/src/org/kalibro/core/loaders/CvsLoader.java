package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.KalibroException;

/**
 * Loader for CVS repositories.
 * 
 * @author Carlos Morais
 */
public class CvsLoader extends RepositoryLoader {

	@Override
	public List<String> validationCommands() {
		return Arrays.asList("cvs --version");
	}

	@Override
	public List<String> loadCommands(String address, boolean update) {
		if (update)
			return Arrays.asList("cvs update");
		return Arrays.asList("cvs -z3 -d " + address + " checkout -d . -P .");
	}

	@Override
	protected String metadataDirectoryName() {
		return "CVSROOT";
	}

	// FIXME cvs is deprecated. Find a better way for historical analysis.
	@Override
	protected List<String> rollBackOneCommit(boolean update) {
		if (! update)
			throw new KalibroException(LOAD_ERROR_MESSAGE);
		throw new KalibroException("Kalibro does not support CVS historic analysis.");
	}

	@Override
	public List<String> returnToLatestCommit() {
		throw new KalibroException("Kalibro does not support CVS historic analysis.");
	}
}