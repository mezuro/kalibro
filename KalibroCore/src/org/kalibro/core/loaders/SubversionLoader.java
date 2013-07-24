package org.kalibro.core.loaders;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.kalibro.KalibroException;
import org.kalibro.core.command.CommandTask;

/**
 * Loader for Subversion repositories.
 * 
 * @author Carlos Morais
 */
public class SubversionLoader extends RepositoryLoader {

	@Override
	public List<String> validationCommands() {
		return Arrays.asList("svn --version");
	}

	@Override
	public List<String> loadCommands(String address, boolean update) {
		if (update)
			return Arrays.asList("svn update");
		return Arrays.asList("svn checkout " + address + " .");
	}

	@Override
	protected String metadataDirectoryName() {
		return ".svn";
	}

	@Override
	protected List<String> rollBackOneCommit(boolean update) throws IOException {
		if (! update)
			throw new KalibroException(LOAD_ERROR_MESSAGE);

		String command = "svn info";
		InputStream data = new CommandTask(command).executeAndGetOuput();
		String svnInfo = IOUtils.toString(data);
		Long previousRevision = getPreviousRevision(svnInfo);
		if (isPossibleToRollBack(previousRevision))
			return Arrays.asList("svn update -r " + previousRevision);
		return null;
	}

	private Long getPreviousRevision(String svnInfo) {
		String previous = " ";
		Long previousRevision = new Long(0);
		for (String element : svnInfo.split(" |\n")) {
			if (previous.matches("Revision:"))
				previousRevision = new Long(element) - 1;
			previous = element;
		}
		return previousRevision;
	}

	private boolean isPossibleToRollBack(Long previousRevision) {
		return previousRevision >= 1;
	}

	@Override
	public List<String> returnToLatestCommit() {
		return Arrays.asList("svn update");
	}
}