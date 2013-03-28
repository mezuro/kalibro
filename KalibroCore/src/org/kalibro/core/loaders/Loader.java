package org.kalibro.core.loaders;

import static java.util.concurrent.TimeUnit.*;

import java.io.File;
import java.util.List;

import org.kalibro.core.command.CommandTask;

/**
 * Abstract loader class. Loader download source code from repositories using system calls.
 * 
 * @author Carlos Morais
 * @author Diego Araújo
 */
public abstract class Loader {

	public void validate() {
		for (String validationCommand : validationCommands())
			new CommandTask(validationCommand).execute(30, SECONDS);
	}

	protected abstract List<String> validationCommands();

	public void load(String address, File loadDirectory) {
		List<String> commands = loadCommands(address, loadDirectory.exists());
		loadDirectory.mkdirs();
		for (String loadCommand : commands)
			new CommandTask(loadCommand, loadDirectory).execute(12, HOURS);
	}

	protected abstract List<String> loadCommands(String address, boolean update);

	public abstract boolean isUpdatable(File directory);
}
