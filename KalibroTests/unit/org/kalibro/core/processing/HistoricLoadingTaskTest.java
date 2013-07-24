package org.kalibro.core.processing;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Project;
import org.kalibro.Repository;
import org.kalibro.RepositoryType;
import org.kalibro.core.loaders.GitLoader;
import org.kalibro.core.loaders.RepositoryLoader;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@PrepareForTest(HistoricLoadingTask.class)
@RunWith(PowerMockRunner.class)
public class HistoricLoadingTaskTest extends UnitTest {

	private static final Long PROJECT_ID = 15L;
	private static final String PROJECT_NAME = "  new Project";

	private static final Long REPOSITORY_ID = 47L;
	private static final String REPOSITORY_NAME = "***NEW REPOSITORY***";

	private RepositoryLoader repositoryLoader;
	private HistoricLoadingTask historicLoadingTask;
	private Repository repository;
	private Project project;

	@Before
	public void setUp() throws Exception {
		historicLoadingTask = spy(new HistoricLoadingTask());
		historicLoadingTask.prepare(mock(ProcessTask.class));
		mockRepositoryLoader();
		mockEntities();
	}

	@Test
	public void shouldLoadRepositoryAndProcess() throws Exception {
		File file = mock(File.class);
		when(historicLoadingTask.codeDirectory()).thenReturn(file);
		when(repositoryLoader.loadForHistoricProcessing(file)).thenReturn(true);
		historicLoadingTask.perform();
		verify(historicLoadingTask, once()).prepareCodeDirectory();
		assertFalse(historicLoadingTask.finishedHistoricProcessing());
		verify(repositoryLoader, never()).returnToLatestCommit();
	}

	@Test
	public void shouldReturnToLatestCommitWhenCouldNotLoadRepository() throws Exception {
		File file = mock(File.class);
		when(historicLoadingTask.codeDirectory()).thenReturn(file);
		when(repositoryLoader.loadForHistoricProcessing(file)).thenReturn(false);
		historicLoadingTask.perform();
		verify(historicLoadingTask, once()).prepareCodeDirectory();
		assertTrue(historicLoadingTask.finishedHistoricProcessing());
		verify(repositoryLoader, once()).returnToLatestCommit();
	}

	@Test
	public void shouldCreateCorrectRepositoryLoader() throws Exception {
		when(repository.getType()).thenReturn(RepositoryType.GIT);
		doCallRealMethod().when(historicLoadingTask, "createLoader");
		assertClassEquals(GitLoader.class, Whitebox.invokeMethod(historicLoadingTask, "createLoader"));
	}

	private void mockRepositoryLoader() throws Exception {
		repositoryLoader = mock(RepositoryLoader.class);
		doReturn(repositoryLoader).when(historicLoadingTask, "createLoader");
	}

	private void mockEntities() {
		project = mock(Project.class);
		repository = mock(Repository.class);
		doReturn(project).when(historicLoadingTask).project();
		doReturn(repository).when(historicLoadingTask).repository();
		when(project.getId()).thenReturn(PROJECT_ID);
		when(repository.getId()).thenReturn(REPOSITORY_ID);
		when(project.getName()).thenReturn(PROJECT_NAME);
		when(repository.getName()).thenReturn(REPOSITORY_NAME);
	}
}
