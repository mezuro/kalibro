package org.kalibro.core.loaders;

import static org.kalibro.core.model.RepositoryFixtures.*;

import java.io.File;

import org.kalibro.core.model.enums.RepositoryType;

public abstract class RemoteFileTest extends LoaderIntegrationTest {

	private static final String TEMPORARY_FILE_PATH = new File(HELLO_WORLD_DIRECTORY, "TEMP").getAbsolutePath();

	@Override
	public void executeCommand(String command) {
		String address = repository.getAddress();
		String downloadCommand = "wget --user=USERNAME --password=PASSWORD " + address + " -O " + TEMPORARY_FILE_PATH;
		if (command.equals(downloadCommand))
			prepareTemporaryFile();
		else
			super.executeCommand(command);
	}

	private void prepareTemporaryFile() {
		RepositoryType localType = RepositoryType.valueOf(getRepositoryType().name().replace("REMOTE", "LOCAL"));
		String localPath = helloWorldRepository(localType).getAddress();
		super.executeCommand("cp " + localPath + " " + TEMPORARY_FILE_PATH);
	}
}