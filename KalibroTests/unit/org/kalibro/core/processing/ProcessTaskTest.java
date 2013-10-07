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
import org.kalibro.Repository;
import org.kalibro.RepositoryObserver;
import org.kalibro.core.concurrent.TaskReport;
import org.kalibro.core.persistence.ProcessingDatabaseDao;
import org.kalibro.core.persistence.RepositoryObserverDatabaseDao;
import org.kalibro.dao.DaoFactory;
import org.kalibro.tests.UnitTest;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ProcessTask.class, DaoFactory.class})
public class ProcessTaskTest extends UnitTest {

	private static final Long EXECUTION_TIME = new Random().nextLong();

	private Repository repository;
	private Processing processing;
	private ProcessContext context;
	private ProcessingDatabaseDao processingDao;
	private SortedSet<RepositoryObserver> observers = new TreeSet<RepositoryObserver>();

	private LoadingTask loadingTask;
	private CollectingTask collectingTask;
	private BuildingTask buildingTask;
	private AggregatingTask aggregatingTask;
	private CalculatingTask calculatingTask;

	private ProcessTask processTask;

	@Before
	public void setUp() throws Exception {
		mockContext();
		processTask = spy(new ProcessTask(repository));
		mockSubtasks();
		processTask.perform();
	}

	private void mockContext() throws Exception {
		repository = mock(Repository.class);
		processing = mock(Processing.class);
		processingDao = mock(ProcessingDatabaseDao.class);
		context = mock(ProcessContext.class);
		RepositoryObserverDatabaseDao repositoryObserverDatabaseDao = mock(RepositoryObserverDatabaseDao.class);
		whenNew(ProcessContext.class).withArguments(repository).thenReturn(context);
		when(context.processingDao()).thenReturn(processingDao);
		when(context.processing()).thenReturn(processing);
		when(repository.getId()).thenReturn(new Random().nextLong());
		when(processing.getState()).thenReturn(ProcessState.LOADING);

		mockStatic(DaoFactory.class);
		when(DaoFactory.getRepositoryObserverDao()).thenReturn(repositoryObserverDatabaseDao);
		when(repositoryObserverDatabaseDao.observersOf(repository.getId())).thenReturn(observers);
	}

	private void mockSubtasks() throws Exception {
		loadingTask = mockSubtask(LoadingTask.class);
		collectingTask = mockSubtask(CollectingTask.class);
		buildingTask = mockSubtask(BuildingTask.class);
		aggregatingTask = mockSubtask(AggregatingTask.class);
		calculatingTask = mockSubtask(CalculatingTask.class);
	}

	private <T extends ProcessSubtask> T mockSubtask(Class<T> subtaskClass) throws Exception {
		T subtask = mock(subtaskClass);
		whenNew(subtaskClass).withArguments(context).thenReturn(subtask);
		when(subtask.addListener(processTask)).thenReturn(subtask);
		return subtask;
	}

	@Test
	public void shouldExecuteSubtasksListeningToThem() {
		InOrder order = Mockito.inOrder(loadingTask, collectingTask, buildingTask, aggregatingTask, calculatingTask);
		order.verify(loadingTask).addListener(processTask);
		order.verify(loadingTask).execute();
		order.verify(collectingTask).addListener(processTask);
		order.verify(collectingTask).executeInBackground();
		order.verify(buildingTask).addListener(processTask);
		order.verify(buildingTask).execute();
		order.verify(aggregatingTask).addListener(processTask);
		order.verify(aggregatingTask).execute();
		order.verify(calculatingTask).addListener(processTask);
		order.verify(calculatingTask).execute();
	}

	@Test
	public void shouldUpdateProcessingOnTaskFinished() {
		when(loadingTask.getState()).thenReturn(ProcessState.LOADING);
		processTask.taskFinished(report(loadingTask, null));

		InOrder order = Mockito.inOrder(processing, processingDao);
		order.verify(processing).setStateTime(ProcessState.LOADING, EXECUTION_TIME);
		order.verify(processing).setState(ProcessState.COLLECTING);
		order.verify(processingDao).save(processing, repository.getId());
	}

	@Test
	public void shouldUpdateProcessingOnTaskHalted() {
		Throwable error = mock(Throwable.class);
		when(collectingTask.getState()).thenReturn(ProcessState.COLLECTING);
		processTask.taskFinished(report(collectingTask, error));

		InOrder order = Mockito.inOrder(processing, processingDao);
		order.verify(processing).setStateTime(ProcessState.COLLECTING, EXECUTION_TIME);
		order.verify(processing).setError(error);
		order.verify(processingDao).save(processing, repository.getId());
	}

	private TaskReport<Void> report(ProcessSubtask subtask, Throwable error) {
		TaskReport<Void> report = mock(TaskReport.class);
		when(report.getExecutionTime()).thenReturn(EXECUTION_TIME);
		when(report.isTaskDone()).thenReturn(error == null);
		when(report.getError()).thenReturn(error);
		when(report.getTask()).thenReturn(subtask);
		return report;
	}

	@Test
	public void shouldNotifyObservers() {
		when(processing.getState()).thenReturn(ProcessState.READY);
		processTask.tryToNotify();
		verify(processTask).notifyObservers();
	}

	@Test
	public void shouldNotNotifyObservers() {
		when(processing.getState()).thenReturn(ProcessState.CALCULATING);
		processTask.tryToNotify();
		verify(processTask, never()).notifyObservers();
	}

	@Test
	public void shouldNotifyAllObservers() {
		RepositoryObserver repositoryObserver = mock(RepositoryObserver.class);
		RepositoryObserver anotherRepositoryObserver = mock(RepositoryObserver.class);
		when(repositoryObserver.compareTo(anotherRepositoryObserver)).thenReturn(1);
		when(anotherRepositoryObserver.compareTo(repositoryObserver)).thenReturn(-1);

		Assert.assertTrue(observers.isEmpty());
		observers.add(repositoryObserver);
		observers.add(anotherRepositoryObserver);
		Assert.assertTrue(observers.contains(repositoryObserver));
		Assert.assertTrue(observers.contains(anotherRepositoryObserver));

		when(processing.getState()).thenReturn(ProcessState.ERROR);
		processTask.notifyObservers();
		for (RepositoryObserver observer : observers)
			verify(observer).update(repository, ProcessState.ERROR);
	}

	@Test
	public void shouldNotNotifyIfThereIsNoObserver() {
		Assert.assertTrue(observers.isEmpty());
		processTask.notifyObservers();
		verify(processing, never()).getState();
	}
}