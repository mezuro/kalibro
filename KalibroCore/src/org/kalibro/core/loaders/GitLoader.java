package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.KalibroException;
import org.kalibro.core.command.CommandTask;

/**
 * Loader for Git repositories.
 * 
 * @author Carlos Morais
 * @author Daniel Alves
 * @author Diego Araújo
 * @author Guilherme Rojas
 */
public class GitLoader extends RepositoryLoader {

	private String latestCommit = "KalibroTag";

	public GitLoader() {
		String command = "git tag " + latestCommit;
		new CommandTask(command).execute();
	}

	@Override
	public List<String> validationCommands() {
		return Arrays.asList("git --version");
	}

	@Override
	protected List<String> loadCommands(String address, boolean update) {
		if (update)
			return Arrays.asList("git pull origin master");
		return Arrays.asList("git clone " + address + " .");
	}

	@Override
	protected String metadataDirectoryName() {
		return ".git";
	}

	@Override
	protected List<String> rollBackOneCommit(boolean update) {
		if (! update)
			throw new KalibroException(LOAD_ERROR_MESSAGE);

		if (isPossibleToRollBack())
			return Arrays.asList("git checkout HEAD~1");
		return null;
	}

	// FIXME
	private boolean isPossibleToRollBack() {
		return true;
	}

	@Override
	protected List<String> returnToLatestCommit() {
		return Arrays.asList("git checkout " + latestCommit);
	}

	// FIXME I need testing
	public void destroyTag() {
		String command = "git tag -d " + latestCommit;
		new CommandTask(command).execute();
	}
}