package org.kalibro.core.loaders;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.kalibro.core.model.Repository;

abstract class RemoteFileLoader extends ProjectLoader {

	private ProjectLoader localLoader = createLocalLoader();

	protected abstract ProjectLoader createLocalLoader();

	@Override
	public List<String> getValidationCommands() {
		List<String> validationCommands = new ArrayList<String>();
		validationCommands.add("wget --version");
		validationCommands.addAll(localLoader.getValidationCommands());
		return validationCommands;
	}

	@Override
	public boolean supportsAuthentication() {
		return true;
	}

	@Override
	public List<String> getLoadCommands(Repository repository, boolean update) {
		String temporaryFilePath = "." + File.separator + "." + hashCode();
		List<String> loadCommands = new ArrayList<String>();
		loadCommands.add(getDownloadCommand(repository, temporaryFilePath));
		loadCommands.addAll(localLoader.getLoadCommands(new Repository(null, temporaryFilePath), update));
		loadCommands.add("rm " + temporaryFilePath);
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