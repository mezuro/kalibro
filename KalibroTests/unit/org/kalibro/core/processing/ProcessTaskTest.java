package org.kalibro.core.processing;

import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.ProcessState;
import org.kalibro.Processing;
import org.kalibro.ProcessingObserver;
import org.kalibro.Repository;
import org.kalibro.core.concurrent.TaskReport;
import org.kalibro.core.persistence.DatabaseDaoFactory;
import org.kalibro.core.persistence.ProcessingDatabaseDao;
import org.kalibro.core.persistence.ProcessingObserverDatabaseDao;
import org.kalibro.dao.DaoFactory;
import org.kalibro.tests.UnitTest;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ProcessTask.class, DaoFactory.class})
public class ProcessTaskTest extends UnitTest {

	private static final Long REPOSITORY_ID = new Random().nextLong();
	private static final Long EXECUTION_TIME = new Random().nextLong();

	private Repository repository;
	private Processing processing;
	private ProcessingDatabaseDao processingDao;
	private SortedSet<ProcessingObserver> observers = new TreeSet<ProcessingObserver>();

	private LoadingTask loadingTask;
	private CollectingTask collectingTask;
	private AnalyzingTask analyzingTask;

	private ProcessTask processTask;

	@Before
	public void setUp() throws Exception {
		mockEntities();
		processTask = spy(new ProcessTask(repository));
		mockSubtasks();
		processTask.perform();
	}

	private void mockEntities() throws Exception {
		repository = mock(Repository.class);
		processing = mock(Processing.class);
		processingDao = mock(ProcessingDatabaseDao.class);
		ProcessingObserverDatabaseDao processingObserverDatabaseDao = mock(ProcessingObserverDatabaseDao.class);

		mockStatic(DaoFactory.class);
		when(DaoFactory.getProcessingObserverDao()).thenReturn(processingObserverDatabaseDao);
		when(processingObserverDatabaseDao.observersOf(REPOSITORY_ID)).thenReturn(observers);

		DatabaseDaoFactory daoFactory = mock(DatabaseDaoFactory.class);
		when(repository.getId()).thenReturn(REPOSITORY_ID);
		whenNew(DatabaseDaoFactory.class).withNoArguments().thenReturn(daoFactory);
		when(daoFactory.createProcessingDao()).thenReturn(processingDao);
		when(processingDao.createProcessingFor(repository)).thenReturn(processing);
	}

	private void mockSubtasks() throws Exception {
		loadingTask = mockSubtask(LoadingTask.class);
		collectingTask = mockSubtask(CollectingTask.class);
		analyzingTask = mockSubtask(AnalyzingTask.class);
	}

	private <T extends ProcessSubtask> T mockSubtask(Class<T> subtaskClass) throws Exception {
		T subtask = mock(subtaskClass);
		whenNew(subtaskClass).withNoArguments().thenReturn(subtask);
		when(subtask.prepare(processTask)).thenReturn(subtask);
		return subtask;
	}

	// @Test
	public void shouldPrepareAndExecuteSubtasks() {
		InOrder order = Mockito.inOrder(loadingTask, collectingTask, analyzingTask);
		order.verify(loadingTask).prepare(processTask);
		order.verify(loadingTask).execute();
		order.verify(collectingTask).prepare(processTask);
		order.verify(collectingTask).executeInBackground();
		order.verify(analyzingTask).prepare(processTask);
		order.verify(analyzingTask).execute();
	}

	@Test
	public void shouldUpdateProcessingOnTaskFinished() {
		when(processing.getState()).thenReturn(ProcessState.LOADING);
		processTask.taskFinished(report(null));

		InOrder order = Mockito.inOrder(processing, processTask, processingDao);
		order.verify(processing).setStateTime(ProcessState.READY, EXECUTION_TIME);
		order.verify(processing).setState(ProcessState.READY.nextState());
		// order.verify(processTask).notifyObservers();
		order.verify(processingDao).save(processing, REPOSITORY_ID);
	}

	// @Test
	public void shouldUpdateProcessingOnTaskHalted() {
		Throwable error = mock(Throwable.class);
		when(processing.getState()).thenReturn(ProcessState.COLLECTING);
		processTask.taskFinished(report(error));

		InOrder order = Mockito.inOrder(processing, processingDao);
		order.verify(processing).setStateTime(ProcessState.READY, EXECUTION_TIME);
		order.verify(processing).setError(error);
		order.verify(processingDao).save(processing, REPOSITORY_ID);
	}

	// @Test
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

	// @Test
	public void shouldNotifyAllObservers() {
		ProcessingObserver processingObserver = mock(ProcessingObserver.class);
		ProcessingObserver anotherProcessingObserver = mock(ProcessingObserver.class);

		Assert.assertTrue(observers.isEmpty());
		observers.add(processingObserver);
		observers.add(anotherProcessingObserver);
		Assert.assertTrue(observers.contains(processingObserver));
		Assert.assertTrue(observers.contains(anotherProcessingObserver));
		System.out.println(observers.size());

		when(processing.getState()).thenReturn(ProcessState.ERROR);
		processTask.notifyObservers();
		for (ProcessingObserver observer : observers) {
			verify(observer).update(repository, ProcessState.ERROR);
		}
	}

	// @Test
	public void shouldNotNotifyIfThereIsNoObserver() {
		Assert.assertTrue(observers.isEmpty());
		processTask.notifyObservers();
		verify(processing, never()).getState();
	}

}