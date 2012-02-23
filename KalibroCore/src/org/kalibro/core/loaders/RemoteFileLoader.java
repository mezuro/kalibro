package org.kalibro.core.loaders;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.kalibro.core.model.Repository;

abstract class RemoteFileLoader implements ProjectLoader {

	@Override
	public List<String> getValidationCommands() {
		return Arrays.asList("wget --version", getExtractorValidationCommand());
	}

	protected abstract String getExtractorValidationCommand();

	@Override
	public boolean supportsAuthentication() {
		return true;
	}

	@Override
	public List<String> getLoadCommands(Repository repository, String loadPath) {
		String temporaryFilePath = new File(new File(loadPath), "TEMP").getAbsolutePath();
		String download = getDownloadCommand(repository, temporaryFilePath);
		String extraction = getExtractionCommand(temporaryFilePath, loadPath);
		String removal = "rm " + temporaryFilePath;
		return Arrays.asList(download, extraction, removal);
	}

	private String getDownloadCommand(Repository repository, String temporaryFilePath) {
		String downloadCommand = "wget ";
		if (repository.hasAuthentication())
			downloadCommand += "--user=" + repository.getUsername() + " --password=" + repository.getPassword() + " ";
		downloadCommand += repository.getAddress() + " -O " + temporaryFilePath;
		return downloadCommand;
	}

	protected abstract String getExtractionCommand(String temporaryFilePath, String loadPath);
}