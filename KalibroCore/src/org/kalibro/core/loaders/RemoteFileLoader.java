package org.kalibro.core.loaders;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.kalibro.Repository;

abstract class RemoteFileLoader extends RepositoryLoader {

	private RepositoryLoader localLoader = createLocalLoader();

	protected abstract RepositoryLoader createLocalLoader();

	@Override
	public List<String> validationCommands() {
		List<String> validationCommands = new ArrayList<String>();
		validationCommands.add("wget --version");
		validationCommands.addAll(localLoader.validationCommands());
		return validationCommands;
	}

	@Override
	public boolean supportsAuthentication() {
		return true;
	}

	@Override
	public List<String> loadCommands(Repository repository, boolean update) {
		String temporaryFilePath = "." + File.separator + "." + hashCode();
		List<String> loadCommands = new ArrayList<String>();
		loadCommands.add(getDownloadCommand(repository, temporaryFilePath));
		loadCommands.addAll(localLoader.loadCommands(new Repository(null, temporaryFilePath), update));
		return loadCommands;
	}

	private String getDownloadCommand(Repository repository, String temporaryFilePath) {
		String downloadCommand = "wget -N ";
		if (repository.hasAuthentication())
			downloadCommand += "--user=" + repository.getUsername() + " --password=" + repository.getPassword() + " ";
		downloadCommand += repository.getAddress() + " -O " + temporaryFilePath;
		return downloadCommand;
	}
}