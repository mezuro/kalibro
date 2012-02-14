package org.kalibro.core.loaders;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public abstract class RemoteFileLoaderTestCase extends ProjectLoaderTestCase {

	@Override
	protected List<String> expectedValidationCommands() {
		return Arrays.asList("wget --version", expectedExtractorValidationCommand());
	}

	protected abstract String expectedExtractorValidationCommand();

	@Override
	protected boolean shouldSupportAuthentication() {
		return true;
	}

	@Override
	protected List<String> expectedLoadCommands(String loadPath) {
		String temporaryFilePath = new File(new File(loadPath), "TEMP").getAbsolutePath();
		String downloadCommand = "wget " +
			"--user=" + repository.getUsername() + " --password=" + repository.getPassword() + " " +
			repository.getAddress() + " -O " + temporaryFilePath;
		String extractionCommand = expectedExtractionCommand(temporaryFilePath, loadPath);
		String removal = "rm " + temporaryFilePath;
		return Arrays.asList(downloadCommand, extractionCommand, removal);
	}

	protected abstract String expectedExtractionCommand(String temporaryFilePath, String loadPath);
}