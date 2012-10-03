package org.kalibro.core.loaders;

import static java.util.concurrent.TimeUnit.*;

import java.io.File;
import java.util.List;

import org.kalibro.KalibroException;
import org.kalibro.Repository;
import org.kalibro.core.command.CommandTask;

public abstract class RepositoryLoader {

	public boolean validate() {
		try {
			executeValidation();
			return true;
		} catch (KalibroException exception) {
			return false;
		}
	}

	private void executeValidation() {
		for (String validationCommand : getValidationCommands())
			new CommandTask(validationCommand).execute(1, MINUTES);
	}

	protected abstract List<String> getValidationCommands();

	public abstract boolean supportsAuthentication();

	public void load(Repository repository, File loadDirectory) {
		List<String> commands = getLoadCommands(repository, loadDirectory.exists());
		loadDirectory.mkdirs();
		for (String loadCommand : commands)
			new CommandTask(loadCommand, loadDirectory).execute(1, HOURS);
	}

	protected abstract List<String> getLoadCommands(Repository repository, boolean update);
}