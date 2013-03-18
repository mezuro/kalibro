package org.kalibro.core.processing;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.*;
import org.kalibro.core.Environment;
import org.kalibro.core.loaders.GitLoader;
import org.kalibro.core.loaders.Loader;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({FileUtils.class, KalibroSettings.class, LoadingTask.class})
public class LoadingTaskTest extends UnitTest {

	private static final Long PROJECT_ID = 6L;
	private static final String PROJECT_NAME = "  my project()";
	private static final String PROJECT_DIRECTORY = "MyProject-6";

	private static final Long REPOSITORY_ID = 28L;
	private static final String REPOSITORY_NAME = "***MY REPOSITORY***";
	private static final String REPOSITORY_DIRECTORY = "MyRepository-28";

	private File loadDirectory;
	private Project project;
	private Repository repository;
	private Loader loader;

	private LoadingTask loadingTask;

	@Before
	public void setUp() throws Exception {
		loadingTask = spy(new LoadingTask());
		loadingTask.prepare(mock(ProcessTask.class));
		mockLoader();
		mockEntities();
		mockLoadDirectory();
	}

	private void mockLoader() throws Exception {
		loader = mock(Loader.class);
		doReturn(loader).when(loadingTask, "createLoader");
	}

	private void mockEntities() {
		project = mock(Project.class);
		repository = mock(Repository.class);
		doReturn(project).when(loadingTask).project();
		doReturn(repository).when(loadingTask).repository();
		when(project.getId()).thenReturn(PROJECT_ID);
		when(repository.getId()).thenReturn(REPOSITORY_ID);
		when(project.getName()).thenReturn(PROJECT_NAME);
		when(repository.getName()).thenReturn(REPOSITORY_NAME);
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

	@After
	public void tearDown() {
		FileUtils.deleteQuietly(loadDirectory);
	}

	@Test
	public void checkPreparedDirectoryNames() throws Exception {
		loadingTask.perform();
		File codeDirectory = loadingTask.codeDirectory();

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
		loadingTask.perform();
		assertFalse(fileA.exists());
		assertFalse(fileB.exists());
	}

	@Test
	public void shouldDeleteIfExistsButIsNotDirectory() throws Exception {
		File file = new File(loadDirectory, "X-" + PROJECT_ID);
		loadDirectory.mkdirs();
		file.createNewFile();
		loadingTask.perform();
		assertFalse(file.exists());
	}

	@Test
	public void shouldLoadWithLoader() throws Exception {
		String address = "My repository address";
		when(repository.getAddress()).thenReturn(address);
		loadingTask.perform();
		File codeDirectory = loadingTask.codeDirectory();
		verify(loader).load(address, codeDirectory);
	}

	@Test
	public void shouldCreateCorrectLoader() throws Exception {
		when(repository.getType()).thenReturn(RepositoryType.GIT);
		doCallRealMethod().when(loadingTask, "createLoader");
		assertClassEquals(GitLoader.class, Whitebox.invokeMethod(loadingTask, "createLoader"));
	}

	@Test
	public void shouldDeleteRepositoryDirectoryIfNotUpdatable() throws Exception {
		File repositoryDirectory = mockRepositoryDirectory();
		when(repositoryDirectory.exists()).thenReturn(true);
		when(repositoryDirectory.isDirectory()).thenReturn(true);
		when(loader.isUpdatable(repositoryDirectory)).thenReturn(false);
		loadingTask.perform();
		verifyStatic();
		FileUtils.deleteQuietly(repositoryDirectory);
	}

	private File mockRepositoryDirectory() throws Exception {
		mockStatic(FileUtils.class);
		File projectDirectory = mock(File.class);
		File repositoryDirectory = mock(File.class);
		whenNew(File.class).withParameterTypes(File.class, String.class)
			.withArguments(eq(loadDirectory), anyString()).thenReturn(projectDirectory);
		when(projectDirectory.listFiles(any(FileFilter.class))).thenReturn(new File[0]);
		whenNew(File.class).withParameterTypes(File.class, String.class)
			.withArguments(eq(projectDirectory), anyString()).thenReturn(repositoryDirectory);
		return repositoryDirectory;
	}
}