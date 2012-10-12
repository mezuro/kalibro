package org.kalibro.core.loaders;

import static java.util.concurrent.TimeUnit.*;

import java.io.File;
import java.util.List;

import org.kalibro.Repository;
import org.kalibro.core.command.CommandTask;

/**
 * Abstract loader class. Assumes that loaders with download source code from repositories using system calls.
 * 
 * @author Carlos Morais
 */
public abstract class RepositoryLoader {

	public boolean validate() {
		try {
			executeValidation();
			return true;
		} catch (Throwable exception) {
			return false;
		}
	}

	private void executeValidation() {
		for (String validationCommand : validationCommands())
			new CommandTask(validationCommand).execute(30, SECONDS);
	}

	protected abstract List<String> validationCommands();

	public void load(Repository repository, File loadDirectory) {
		List<String> commands = loadCommands(repository, loadDirectory.exists());
		loadDirectory.mkdirs();
		for (String loadCommand : commands)
			new CommandTask(loadCommand, loadDirectory).execute(10, HOURS);
	}

	protected abstract List<String> loadCommands(Repository repository, boolean update);
}