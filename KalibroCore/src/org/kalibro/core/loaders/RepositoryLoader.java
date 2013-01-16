package org.kalibro.core.loaders;

import static java.util.concurrent.TimeUnit.*;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.kalibro.core.command.CommandTask;

/**
 * Abstract loader class. Loader download source code from repositories using system calls.
 * 
 * @author Carlos Morais
 */
public abstract class RepositoryLoader {

	public void validate() {
		for (String validationCommand : validationCommands())
			new CommandTask(validationCommand).execute(30, SECONDS);
	}

	protected abstract List<String> validationCommands();

	public void load(String address, File loadDirectory) {
		List<String> commands = loadCommands(address, loadDirectory.exists());
		loadDirectory.mkdirs();
		for (String loadCommand : commands)
			new CommandTask(loadCommand, loadDirectory).execute(10, HOURS);
	}

	protected abstract List<String> loadCommands(String address, boolean update);
	
	public boolean isUpdatable(File directory) {
		NameFileFilter nameFilter = new NameFileFilter(metadataDirectoryName());
		return FileUtils.iterateFiles(directory, FalseFileFilter.INSTANCE, nameFilter).hasNext();
	}

	protected abstract String metadataDirectoryName();
}