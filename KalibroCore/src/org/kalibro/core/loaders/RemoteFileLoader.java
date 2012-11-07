package org.kalibro.core.loaders;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract loader for remote compressed files.
 * 
 * @author Carlos Morais
 */
abstract class RemoteFileLoader extends RepositoryLoader {

	@Override
	public List<String> validationCommands() {
		return mergeCommands("wget --version", localLoader().validationCommands(), "rm --version");
	}

	@Override
	public List<String> loadCommands(String address, boolean update) {
		String temporaryFilePath = "." + File.separator + "." + hashCode();
		return mergeCommands(
			"wget " + address + " -O " + temporaryFilePath,
			localLoader().loadCommands(temporaryFilePath, update),
			"rm -f " + temporaryFilePath);
	}

	private List<String> mergeCommands(String firstCommand, List<String> commands, String lastCommand) {
		List<String> merged = new ArrayList<String>();
		merged.add(firstCommand);
		merged.addAll(commands);
		merged.add(lastCommand);
		return merged;
	}

	protected abstract RepositoryLoader localLoader();
}