package org.kalibro.core.loaders;

import java.util.List;

import org.junit.Test;

public abstract class RemoteFileLoaderTestCase extends RepositoryLoaderTestCase {

	@Override
	protected List<String> expectedValidationCommands() {
		List<String> commands = list("wget --version", "rm --version");
		commands.addAll(1, loader().localLoader().validationCommands());
		return commands;
	}

	@Override
	protected List<String> expectedLoadCommands() {
		return expectedLoadCommands(false);
	}

	@Override
	protected List<String> expectedUpdateCommands() {
		return expectedLoadCommands(true);
	}

	private List<String> expectedLoadCommands(boolean update) {
		String temporaryFilePath = "./." + loader().hashCode();
		List<String> commands = list("wget " + ADDRESS + " -O " + temporaryFilePath, "rm -f " + temporaryFilePath);
		commands.addAll(1, loader().localLoader().loadCommands(temporaryFilePath, update));
		return commands;
	}

	@Test
	public void checkLocalLocader() {
		assertClassEquals(expectedLocalLoader(), loader().localLoader());
	}

	private RemoteFileLoader loader() {
		return (RemoteFileLoader) loader;
	}

	protected abstract Class<? extends RepositoryLoader> expectedLocalLoader();
}