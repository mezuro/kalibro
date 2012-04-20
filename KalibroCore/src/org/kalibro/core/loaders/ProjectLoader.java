package org.kalibro.core.loaders;

import java.io.File;
import java.util.List;

import org.kalibro.KalibroException;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.Repository;

public abstract class ProjectLoader {

	protected static final long LOAD_TIMEOUT = 1 * Task.HOUR;
	protected static final long VALIDATION_TIMEOUT = 1 * Task.MINUTE;

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
			new CommandTask(validationCommand).executeAndWait(VALIDATION_TIMEOUT);
	}

	protected abstract List<String> getValidationCommands();

	public abstract boolean supportsAuthentication();

	public void load(Repository repository, File loadDirectory) {
		List<String> commands = getLoadCommands(repository, loadDirectory.exists());
		loadDirectory.mkdirs();
		for (String loadCommand : commands)
			new CommandTask(loadCommand, loadDirectory).executeAndWait(LOAD_TIMEOUT);
	}

	protected abstract List<String> getLoadCommands(Repository repository, boolean update);
}