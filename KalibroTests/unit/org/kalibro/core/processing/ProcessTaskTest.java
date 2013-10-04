package org.kalibro.core.processing;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.ProcessState;
import org.kalibro.Processing;
import org.kalibro.Repository;
import org.kalibro.core.concurrent.TaskReport;
import org.kalibro.core.persistence.DatabaseDaoFactory;
import org.kalibro.core.persistence.ProcessingDatabaseDao;
import org.kalibro.tests.UnitTest;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ProcessTask.class)
public class ProcessTaskTest extends UnitTest {

	private static final Long REPOSITORY_ID = new Random().nextLong();
	private static final Long EXECUTION_TIME = new Random().nextLong();

	private Repository repository;
	private Processing processing;
	private ProcessingDatabaseDao processingDao;

	private LoadingTask loadingTask;
	private CollectingTask collectingTask;
	private AggregatingTask aggregatingTask;

	private ProcessTask processTask;

	@Before
	public void setUp() throws Exception {
		mockEntities();
		processTask = new ProcessTask(repository);
		mockSubtasks();
		processTask.perform();
	}

	private void mockEntities() throws Exception {
		repository = mock(Repository.class);
		processing = mock(Processing.class);
		processingDao = mock(ProcessingDatabaseDao.class);
		DatabaseDaoFactory daoFactory = mock(DatabaseDaoFactory.class);

		when(repository.getId()).thenReturn(REPOSITORY_ID);
		whenNew(DatabaseDaoFactory.class).withNoArguments().thenReturn(daoFactory);
		when(daoFactory.createProcessingDao()).thenReturn(processingDao);
		when(processingDao.createProcessingFor(repository)).thenReturn(processing);
	}

	private void mockSubtasks() throws Exception {
		loadingTask = mockSubtask(LoadingTask.class);
		collectingTask = mockSubtask(CollectingTask.class);
		aggregatingTask = mockSubtask(AggregatingTask.class);
	}

	private <T extends ProcessSubtask> T mockSubtask(Class<T> subtaskClass) throws Exception {
		T subtask = mock(subtaskClass);
		whenNew(subtaskClass).withNoArguments().thenReturn(subtask);
		when(subtask.prepare(processTask)).thenReturn(subtask);
		return subtask;
	}

	@Test
	public void shouldPrepareAndExecuteSubtasks() {
		InOrder order = Mockito.inOrder(loadingTask, collectingTask, aggregatingTask);
		order.verify(loadingTask).prepare(processTask);
		order.verify(loadingTask).execute();
		order.verify(collectingTask).prepare(processTask);
		order.verify(collectingTask).executeInBackground();
		order.verify(aggregatingTask).prepare(processTask);
		order.verify(aggregatingTask).execute();
	}

	@Test
	public void shouldUpdateProcessingOnTaskFinished() {
		when(processing.getState()).thenReturn(ProcessState.LOADING);
		processTask.taskFinished(report(null));

		InOrder order = Mockito.inOrder(processing, processingDao);
		order.verify(processing).setStateTime(ProcessState.READY, EXECUTION_TIME);
		order.verify(processing).setState(ProcessState.READY.nextState());
		order.verify(processingDao).save(processing, REPOSITORY_ID);
	}

	@Test
	public void shouldUpdateProcessingOnTaskHalted() {
		Throwable error = mock(Throwable.class);
		when(processing.getState()).thenReturn(ProcessState.COLLECTING);
		processTask.taskFinished(report(error));

		InOrder order = Mockito.inOrder(processing, processingDao);
		order.verify(processing).setStateTime(ProcessState.READY, EXECUTION_TIME);
		order.verify(processing).setError(error);
		order.verify(processingDao).save(processing, REPOSITORY_ID);
	}

	@Test
	public void shouldNotUpdateStateOnlyIfCurrentStateIsTemporary() {
		when(processing.getState()).thenReturn(ProcessState.ERROR);
		processTask.taskFinished(report(null));
		verify(processing, never()).setState(any(ProcessState.class));
		verify(processing, never()).setError(any(Throwable.class));
	}

	private TaskReport<Void> report(Throwable error) {
		TaskReport<Void> report = mock(TaskReport.class);
		when(report.getExecutionTime()).thenReturn(EXECUTION_TIME);
		when(report.isTaskDone()).thenReturn(error == null);
		when(report.getError()).thenReturn(error);
		when(report.getTask()).thenReturn(new ReadyTask());
		return report;
	}
}