package org.kalibro.core.loaders;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.kalibro.KalibroException;
import org.kalibro.core.command.CommandTask;

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
	protected List<String> rollBackOneCommit(boolean update) throws IOException {
		if (! update)
			throw new KalibroException(LOAD_ERROR_MESSAGE);

		String command = "bzr revno --tree";
		InputStream data = new CommandTask(command).executeAndGetOuput();
		Long previousRevision = new Long(IOUtils.toString(data)) - 1;
		if (isPossibleToRollBack(previousRevision))
			return Arrays.asList("bzr update -r " + previousRevision);
		return null;
	}

	private boolean isPossibleToRollBack(Long previousRevision) {
		return previousRevision >= 1;
	}

	@Override
	public List<String> returnToLatestCommit() {
		return Arrays.asList("bzr update");
	}
}