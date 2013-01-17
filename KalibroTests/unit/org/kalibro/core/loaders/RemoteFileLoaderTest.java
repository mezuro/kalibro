package org.kalibro.core.loaders;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class RemoteFileLoaderTest extends UnitTest {

	private static final String ADDRESS = "ADDRESS";
	private static final String LOCAL_LOAD_COMMAND = "LOCAL_LOAD_COMMAND";
	private static final String LOCAL_UPDATE_COMMAND = "LOCAL_UPDATE_COMMAND";
	private static final String LOCAL_VALIDATION_COMMAND = "LOCAL_VALIDATION_COMMAND";

	private FileLoader localLoader;
	private String temporaryFilePath;

	private RemoteFileLoader remoteLoader;

	@Before
	public void setUp() throws Exception {
		localLoader = mock(FileLoader.class);
		remoteLoader = mockAbstract(RemoteFileLoader.class);
		doReturn(localLoader).when(remoteLoader).localLoader();

		temporaryFilePath = "./." + remoteLoader.hashCode();
		when(localLoader.validationCommands()).thenReturn(list(LOCAL_VALIDATION_COMMAND));
		when(localLoader.loadCommands(temporaryFilePath, false)).thenReturn(list(LOCAL_LOAD_COMMAND));
		when(localLoader.loadCommands(temporaryFilePath, true)).thenReturn(list(LOCAL_UPDATE_COMMAND));
	}

	@Test
	public void checkValidationCommands() {
		assertDeepEquals(list("wget --version", LOCAL_VALIDATION_COMMAND, "rm --version"),
			remoteLoader.validationCommands());
	}

	@Test
	public void checkLoadCommands() {
		checkMergedCommands(false, LOCAL_LOAD_COMMAND);
		checkMergedCommands(true, LOCAL_UPDATE_COMMAND);
	}

	private void checkMergedCommands(boolean update, String localCommand) {
		assertDeepEquals(list(
			"wget " + ADDRESS + " -O " + temporaryFilePath,
			localCommand,
			"rm -f " + temporaryFilePath),
			remoteLoader.loadCommands(ADDRESS, update));
	}

	@Test
	public void shouldNotBeUpdatable() {
		assertFalse(remoteLoader.isUpdatable(null));
	}

	@Test
	public void shouldNotHaveMetadataDirectoryName() {
		assertNull(remoteLoader.metadataDirectoryName());
	}
}