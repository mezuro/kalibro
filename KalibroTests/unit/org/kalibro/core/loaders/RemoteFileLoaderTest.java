package org.kalibro.core.loaders;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class RemoteFileLoaderTest extends UnitTest {

	private static final String ADDRESS = "ADDRESS";
	private static final String LOCAL_LOAD_COMMAND = "LOCAL_LOAD_COMMAND";
	private static final String LOCAL_UPDATE_COMMAND = "LOCAL_UPDATE_COMMAND";
	private static final String LOCAL_VALIDATION_COMMAND = "LOCAL_VALIDATION_COMMAND";

	private RepositoryLoader localLoader;
	private RemoteFileLoader remoteLoader;
	private String temporaryFilePath;

	@Before
	public void setUp() {
		localLoader = mock(RepositoryLoader.class);
		remoteLoader = new FakeRemoteLoader();
		temporaryFilePath = "./." + remoteLoader.hashCode();
		when(localLoader.validationCommands()).thenReturn(asList(LOCAL_VALIDATION_COMMAND));
		when(localLoader.loadCommands(temporaryFilePath, false)).thenReturn(asList(LOCAL_LOAD_COMMAND));
		when(localLoader.loadCommands(temporaryFilePath, true)).thenReturn(asList(LOCAL_UPDATE_COMMAND));
	}

	@Test
	public void checkValidationCommands() {
		assertDeepEquals(asList("wget --version", LOCAL_VALIDATION_COMMAND, "rm --version"),
			remoteLoader.validationCommands());
	}

	@Test
	public void checkLoadCommands() {
		checkMergedCommands(false, LOCAL_LOAD_COMMAND);
		checkMergedCommands(true, LOCAL_UPDATE_COMMAND);
	}

	private void checkMergedCommands(boolean update, String localCommand) {
		assertDeepEquals(asList(
			"wget " + ADDRESS + " -O " + temporaryFilePath,
			localCommand,
			"rm -f " + temporaryFilePath),
			remoteLoader.loadCommands(ADDRESS, update));
	}

	private class FakeRemoteLoader extends RemoteFileLoader {

		@Override
		protected RepositoryLoader localLoader() {
			return localLoader;
		}
	}
}