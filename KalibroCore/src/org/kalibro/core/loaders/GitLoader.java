package org.kalibro.core.loaders;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
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

	private String branch;

	public GitLoader() throws IOException {
		String command = "git rev-parse --abbrev-ref HEAD";
		branch = IOUtils.toString(new CommandTask(command).executeAndGetOuput());
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
	protected List<String> rollBackOneCommit(boolean update) throws IOException {
		if (! update)
			throw new KalibroException(LOAD_ERROR_MESSAGE);
		String command = "git checkout HEAD~1";
		InputStream commandOutput = new CommandTask(command).executeAndGetOuput();
		if (cannotRollBack(commandOutput))
			return null;
		String revert = "git checkout HEAD@{1}";
		new CommandTask(revert).execute();
		return Arrays.asList(command);
	}

	private Boolean cannotRollBack(InputStream commandOutput) throws IOException {
		return IOUtils.toString(commandOutput).contains(
			"error: pathspec 'HEAD~1' did not match any file(s) known to git.");
	}

	@Override
	public List<String> returnToLatestCommit() {
		return Arrays.asList("git checkout " + branch);
	}
}