package org.kalibro.core.processing;

import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Processing;
import org.kalibro.Repository;
import org.kalibro.RepositoryObserver;
import org.kalibro.core.persistence.DatabaseDaoFactory;
import org.kalibro.core.persistence.ProcessingDatabaseDao;
import org.kalibro.core.persistence.RepositoryObserverDatabaseDao;
import org.kalibro.dao.DaoFactory;
import org.kalibro.tests.UnitTest;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HistoricProcessTask.class, DaoFactory.class})
public class HistoricProcessTaskTest extends UnitTest {

	private static final Long REPOSITORY_ID = new Random().nextLong();

	private Repository repository;
	private Processing processing;
	private ProcessingDatabaseDao processingDao;
	private SortedSet<RepositoryObserver> observers = new TreeSet<RepositoryObserver>();

	private HistoricLoadingTask historicLoadingTask;
	private CollectingTask collectingTask;
	private AnalyzingTask analyzingTask;

	private HistoricProcessTask historicProcessTask;

	@Before
	public void setUp() throws Exception {
		mockEntities();
		historicProcessTask = spy(new HistoricProcessTask(repository));
		mockSubtasks();
		historicProcessTask.perform();
	}

	private void mockEntities() throws Exception {
		repository = mock(Repository.class);
		processing = mock(Processing.class);
		processingDao = mock(ProcessingDatabaseDao.class);
		RepositoryObserverDatabaseDao repositoryObserverDatabaseDao = mock(RepositoryObserverDatabaseDao.class);

		mockStatic(DaoFactory.class);
		when(DaoFactory.getRepositoryObserverDao()).thenReturn(repositoryObserverDatabaseDao);
		when(repositoryObserverDatabaseDao.observersOf(REPOSITORY_ID)).thenReturn(observers);

		DatabaseDaoFactory daoFactory = mock(DatabaseDaoFactory.class);
		when(repository.getId()).thenReturn(REPOSITORY_ID);
		whenNew(DatabaseDaoFactory.class).withNoArguments().thenReturn(daoFactory);
		when(daoFactory.createProcessingDao()).thenReturn(processingDao);
		when(processingDao.createProcessingFor(repository)).thenReturn(processing);
	}

	private void mockSubtasks() throws Exception {
		historicLoadingTask = mockSubtask(HistoricLoadingTask.class);
		when(historicLoadingTask.finishedHistoricProcessing()).thenReturn(false).thenReturn(true);
		collectingTask = mockSubtask(CollectingTask.class);
		analyzingTask = mockSubtask(AnalyzingTask.class);
	}

	private <T extends ProcessSubtask> T mockSubtask(Class<T> subtaskClass) throws Exception {
		T subtask = mock(subtaskClass);
		whenNew(subtaskClass).withNoArguments().thenReturn(subtask);
		when(subtask.prepare(historicProcessTask)).thenReturn(subtask);
		return subtask;
	}

	@Test
	public void shouldPrepareAndExecuteSubtasks() {
		InOrder order = Mockito.inOrder(historicLoadingTask, collectingTask, analyzingTask);
		order.verify(historicLoadingTask).prepare(historicProcessTask);
		order.verify(historicLoadingTask).execute();
		order.verify(collectingTask).prepare(historicProcessTask);
		order.verify(collectingTask).executeInBackground();
		order.verify(analyzingTask).prepare(historicProcessTask);
		order.verify(analyzingTask).execute();
		order.verify(historicLoadingTask).prepare(historicProcessTask);
		order.verify(historicLoadingTask).execute();
	}
}
