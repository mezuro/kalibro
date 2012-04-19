package org.kalibro.core.loaders;

import java.io.File;
import java.util.List;

import org.kalibro.KalibroError;
import org.kalibro.KalibroException;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.Repository;

public abstract class ProjectLoader {
	
	protected static final long LOAD_TIMEOUT = 1 * Task.HOUR;
	protected static final long VALIDATION_TIMEOUT = 1 * Task.MINUTE;

	//TODO Create test for this class
	
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

	public abstract boolean supportsAuthentication();

	public void load(Repository repository, File loadDirectory) {
		List<String> commands;
		if (loadDirectory.exists())
			commands = getUpdateCommands(repository, loadDirectory.getAbsolutePath());
		else {
			commands = getLoadCommands(repository, loadDirectory.getAbsolutePath());
			loadDirectory.mkdirs();
		}

		for (String loadCommand : commands)
			new CommandTask(loadCommand, loadDirectory).executeAndWait(LOAD_TIMEOUT);
	}
	
	protected List<String> getValidationCommands() {
		throw new KalibroError("Not implement.");
	}

	@SuppressWarnings("unused")
	protected List<String> getLoadCommands(Repository repository, String loadPath) {
		throw new KalibroError("Not implement.");
	}

	protected List<String> getUpdateCommands(Repository repository, String loadPath) {
		return getLoadCommands(repository, loadPath);
	}

}