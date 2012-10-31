package org.kalibro.core.processing;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.*;
import org.kalibro.core.Environment;
import org.kalibro.core.loaders.GitLoader;
import org.kalibro.core.loaders.RepositoryLoader;
import org.kalibro.core.persistence.DatabaseDaoFactory;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({KalibroSettings.class, LoadSourceTask.class, ProcessSubtask.class})
public class LoadSourceTaskTest extends UnitTest {

	private static final Long PROJECT_ID = 6L;
	private static final String PROJECT_NAME = "  my project()";
	private static final String PROJECT_DIRECTORY = "MyProject-6";

	private static final Long REPOSITORY_ID = 28L;
	private static final String REPOSITORY_NAME = "***MY REPOSITORY***";
	private static final String REPOSITORY_DIRECTORY = "MyRepository-28";

	private File loadDirectory;
	private Repository repository;
	private RepositoryLoader loader;

	private LoadSourceTask loadTask;

	@Before
	public void setUp() throws Exception {
		mockLoadDirectory();
		whenNew(DatabaseDaoFactory.class).withNoArguments().thenReturn(mock(DatabaseDaoFactory.class));
		loader = mock(RepositoryLoader.class);
		loadTask = spy(new LoadSourceTask(mockProcessing()));
		doReturn(loader).when(loadTask, "createLoader");
		mockNames();
	}

	private void mockLoadDirectory() {
		loadDirectory = new File(Environment.dotKalibro(), "projects");
		ServerSettings serverSettings = mock(ServerSettings.class);
		KalibroSettings kalibroSettings = mock(KalibroSettings.class);
		mockStatic(KalibroSettings.class);
		when(KalibroSettings.load()).thenReturn(kalibroSettings);
		when(kalibroSettings.getServerSettings()).thenReturn(serverSettings);
		when(serverSettings.getLoadDirectory()).thenReturn(loadDirectory);
	}

	private Processing mockProcessing() {
		repository = mock(Repository.class);
		Processing processing = mock(Processing.class);
		when(processing.getRepository()).thenReturn(repository);
		return processing;
	}

	private void mockNames() {
		Project project = mock(Project.class);
		when(repository.getProject()).thenReturn(project);
		when(project.getId()).thenReturn(PROJECT_ID);
		when(project.getName()).thenReturn(PROJECT_NAME);
		when(repository.getId()).thenReturn(REPOSITORY_ID);
		when(repository.getName()).thenReturn(REPOSITORY_NAME);
	}

	@After
	public void tearDown() {
		FileUtils.deleteQuietly(loadDirectory);
	}

	@Test
	public void checkPreparedDirectoryNames() throws Exception {
		File codeDirectory = loadTask.compute();
		assertEquals(REPOSITORY_DIRECTORY, codeDirectory.getName());
		assertEquals(PROJECT_DIRECTORY, codeDirectory.getParentFile().getName());
		assertEquals(loadDirectory, codeDirectory.getParentFile().getParentFile());
	}

	@Test
	public void shouldDeleteIfManyWithSameSuffix() throws Exception {
		File fileA = new File(loadDirectory, "A-" + PROJECT_ID);
		File fileB = new File(loadDirectory, "B-" + PROJECT_ID);
		fileA.mkdirs();
		fileB.mkdirs();
		loadTask.compute();
		assertFalse(fileA.exists());
		assertFalse(fileB.exists());
	}

	@Test
	public void shouldDeleteIfExistsButIsNotDirectory() throws Exception {
		File file = new File(loadDirectory, "X-" + PROJECT_ID);
		loadDirectory.mkdirs();
		file.createNewFile();
		loadTask.compute();
		assertFalse(file.exists());
	}

	@Test
	public void shouldLoadWithLoader() throws Exception {
		String address = "My repository address";
		when(repository.getAddress()).thenReturn(address);
		File codeDirectory = loadTask.compute();
		verify(loader).load(address, codeDirectory);
	}

	@Test
	public void shouldCreateCorrectLoader() throws Exception {
		when(repository.getType()).thenReturn(RepositoryType.GIT);
		doCallRealMethod().when(loadTask, "createLoader");
		assertClassEquals(GitLoader.class, Whitebox.invokeMethod(loadTask, "createLoader"));
	}

	@Test
	public void taskStateShouldBeLoading() {
		assertEquals(ProcessState.LOADING, loadTask.getTaskState());
	}
}