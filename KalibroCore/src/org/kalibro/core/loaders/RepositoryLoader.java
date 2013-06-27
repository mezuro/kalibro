package org.kalibro.core.loaders;

import static java.util.concurrent.TimeUnit.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.kalibro.core.command.CommandTask;

/**
 * Abstract loader for version control systems.
 * 
 * @author Carlos Morais
 * @author Daniel Alves
 * @author Diego Araújo
 * @author Guilherme Rojas
 */
public abstract class RepositoryLoader extends Loader {

	protected static final String LOAD_ERROR_MESSAGE =
		"Repository content has not been loaded yet.";

	@Override
	protected boolean isUpdatable(File directory) {
		NameFileFilter nameFilter = new NameFileFilter(metadataDirectoryName());
		return FileUtils.iterateFiles(directory, FalseFileFilter.INSTANCE, nameFilter).hasNext();
	}

	protected abstract String metadataDirectoryName();

	protected abstract List<String> rollBackOneCommit(boolean update) throws IOException;

	protected abstract List<String> returnToLatestCommit();

	public boolean loadForHistoricProcessing(File loadDirectory) throws IOException {
		List<String> commands = rollBackOneCommit(isUpdatable(loadDirectory));
		if (commands != null) {
			for (String loadCommand : commands)
				new CommandTask(loadCommand, loadDirectory).execute(12, HOURS);
			return true;
		}
		return false;
	}
}