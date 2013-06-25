package org.kalibro.core.loaders;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.kalibro.KalibroException;
import org.kalibro.core.command.CommandTask;

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
	protected List<String> rollBackOneCommit(boolean update) throws IOException {
		if (! update)
			throw new KalibroException(LOAD_ERROR_MESSAGE);

		String command = "svn info | grep Revision | cut -d' ' -f2";
		InputStream data = new CommandTask(command).executeAndGetOuput();
		Long previousRevision = new Long(data.read() - 1);
		return Arrays.asList("svn update -r " + previousRevision);
	}

	@Override
	protected List<String> returnToLatestCommit() {
		return Arrays.asList("svn update");
	}
}