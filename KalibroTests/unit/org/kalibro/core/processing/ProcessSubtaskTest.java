package org.kalibro.core.processing;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.*;
import org.kalibro.core.concurrent.Producer;
import org.kalibro.core.persistence.DatabaseDaoFactory;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ProcessSubtask.class)
public class ProcessSubtaskTest extends UnitTest {

	private Processing processing;
	private Repository repository;
	private ProcessTask mainTask;

	private ProcessSubtask subtask;

	@Before
	public void setUp() {
		processing = mock(Processing.class);
		repository = mock(Repository.class);
		mainTask = mock(ProcessTask.class);
		mainTask.processing = processing;
		mainTask.repository = repository;
		subtask = new ReadyTask();
		assertSame(subtask, subtask.prepare(mainTask));
	}

	@Test
	public void shouldAddMainTaskAsListener() {
		assertDeepEquals(set(mainTask), Whitebox.getInternalState(subtask, "listeners"));
	}

	@Test
	public void shouldGetCodeDirectoryFromMainTask() {
		File codeDirectory = mock(File.class);
		mainTask.codeDirectory = codeDirectory;
		assertSame(codeDirectory, subtask.codeDirectory());
	}

	@Test
	public void shouldSetCodeDirectoryOnMainTask() {
		File codeDirectory = mock(File.class);
		subtask.setCodeDirectory(codeDirectory);
		assertSame(codeDirectory, mainTask.codeDirectory);
	}

	@Test
	public void shouldGetProcessingRepositoryAndProjectFromMainTask() {
		assertSame(processing, subtask.processing());
		assertSame(repository, subtask.repository());

		Project project = mock(Project.class);
		when(repository.getProject()).thenReturn(project);
		assertSame(project, subtask.project());
	}

	@Test
	public void shouldGetDaoFactoryFromMainTask() {
		DatabaseDaoFactory daoFactory = mock(DatabaseDaoFactory.class);
		mainTask.daoFactory = daoFactory;
		assertSame(daoFactory, subtask.daoFactory());
	}

	@Test
	public void shouldGetResultProducerFromMainTask() {
		Producer<NativeModuleResult> resultProducer = mock(Producer.class);
		mainTask.resultProducer = resultProducer;
		assertSame(resultProducer, subtask.resultProducer());
	}

	@Test
	public void toStringShouldBeStateMessage() {
		String name = "ProcessingTest repository complete name";
		when(repository.getCompleteName()).thenReturn(name);

		for (ProcessState state : ProcessState.values()) {
			when(processing.getState()).thenReturn(state);
			assertEquals(state.getMessage(name), "" + subtask);
		}
	}
}