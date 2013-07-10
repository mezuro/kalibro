package org.kalibro.core.loaders;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.kalibro.KalibroException;
import org.kalibro.core.command.CommandTask;

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
	protected List<String> rollBackOneCommit(boolean update) throws IOException {
		if (! update)
			throw new KalibroException(LOAD_ERROR_MESSAGE);

		String command = "hg id -n";
		InputStream data = new CommandTask(command).executeAndGetOuput();
		Long previousRevision = new Long(data.read() - 1);
		if (isPossibleToRollBack(previousRevision))
			return Arrays.asList("hg update " + previousRevision);
		return null;
	}

	private boolean isPossibleToRollBack(Long previousRevision) {
		return previousRevision >= 1;
	}

	@Override
	protected List<String> returnToLatestCommit() {
		return Arrays.asList("hg update");
	}
}