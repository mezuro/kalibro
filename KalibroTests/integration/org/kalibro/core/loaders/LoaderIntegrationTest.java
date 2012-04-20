package org.kalibro.core.loaders;

import static org.junit.Assert.*;
import static org.kalibro.core.model.RepositoryFixtures.*;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.command.FileProcessStreamLogger;
import org.kalibro.core.model.Repository;
import org.kalibro.core.model.enums.RepositoryType;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FileProcessStreamLogger.class)
public abstract class LoaderIntegrationTest extends KalibroTestCase {

	protected ProjectLoader loader;
	protected Repository repository;

	@Before
	public void setUp() {
		// TODO adapt
		// loader = getRepositoryType().getProjectLoader();
		repository = helloWorldRepository(getRepositoryType());
		if (loader.supportsAuthentication()) {
			repository.setUsername("USERNAME");
			repository.setPassword("PASSWORD");
		}
		HELLO_WORLD_DIRECTORY.mkdirs();
		MemberModifier.suppress(FileProcessStreamLogger.class.getMethods());
	}

	protected abstract RepositoryType getRepositoryType();

	@After
	public void tearDown() throws IOException {
		FileUtils.cleanDirectory(PROJECTS_DIRECTORY);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void validateLoaderInstallation() {
		for (String validationCommand : loader.getValidationCommands())
			executeCommand(validationCommand);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testLoad() {
		executeLoadCommands();
		File loaded = assertLoaded();
		executeUpdateCommands();
		File updated = assertLoaded();
		assertEquals(updated.lastModified(), loaded.lastModified());
	}

	private void executeLoadCommands() {
//		for (String loadCommand : loader.getLoadCommands(repository, HELLO_WORLD_DIRECTORY.getAbsolutePath()))
//			executeCommand(loadCommand);
	}

	private void executeUpdateCommands() {
//		for (String loadCommand : loader.getUpdateCommands(repository, HELLO_WORLD_DIRECTORY.getAbsolutePath()))
//			executeCommand(loadCommand);
	}

	protected void executeCommand(String command) {
		new CommandTask(command).executeAndWait(INTEGRATION_TIMEOUT);
	}

	private File assertLoaded() {
		Iterator<File> files = FileUtils.iterateFiles(HELLO_WORLD_DIRECTORY, new String[]{"c"}, true);
		File loaded = files.next();
		assertEquals("HelloWorld.c", loaded.getName());
		assertFalse(files.hasNext());
		return loaded;
	}
}