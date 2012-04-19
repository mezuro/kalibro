package org.kalibro.core.processing;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ProjectFixtures.*;
import static org.kalibro.core.model.RepositoryFixtures.*;
import static org.kalibro.core.model.enums.RepositoryType.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Kalibro;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.command.FileProcessStreamLogger;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.enums.RepositoryType;
import org.kalibro.core.settings.KalibroSettings;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FileProcessStreamLogger.class, Kalibro.class})
public class LoadTest extends KalibroTestCase {

	private Project project;
	private LoadProjectTask loadTask;

	@Before
	public void setUp() {
		project = helloWorld();
		loadTask = new LoadProjectTask(project);
		replaceLoadDirectory();
		MemberModifier.suppress(FileProcessStreamLogger.class.getMethods());
	}

	private void replaceLoadDirectory() {
		KalibroSettings settings = PowerMockito.mock(KalibroSettings.class);
		PowerMockito.mockStatic(Kalibro.class);
		PowerMockito.when(Kalibro.currentSettings()).thenReturn(settings);
		PowerMockito.when(settings.getLoadDirectoryFor(project)).thenReturn(HELLO_WORLD_DIRECTORY);
	}

	@After
	public void tearDown() {
		assertTrue(HELLO_WORLD_DIRECTORY.exists());
		FileUtils.deleteQuietly(HELLO_WORLD_DIRECTORY);
	}

	@Test(timeout = ACCEPTANCE_TIMEOUT /* testing all repository types */)
	public void testLoad() throws Exception {
		for (RepositoryType repositoryType : RepositoryType.values())
			if (!Arrays.asList(REMOTE_TARBALL, REMOTE_ZIP).contains(repositoryType))
				executeAndVerifyProjectFile(repositoryType);
	}

	private void executeAndVerifyProjectFile(RepositoryType repositoryType) throws IOException {
		project.setRepository(helloWorldRepository(repositoryType));
		loadTask.perform();
		Iterator<?> files = FileUtils.iterateFiles(HELLO_WORLD_DIRECTORY, new String[]{"c"}, true);
		assertEquals("HelloWorld.c", ((File) files.next()).getName());
		assertFalse(files.hasNext());
	}
}