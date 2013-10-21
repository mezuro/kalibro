package org.kalibro.core.processing;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.ProcessState;
import org.kalibro.Processing;
import org.kalibro.Repository;
import org.kalibro.core.concurrent.TaskReport;
import org.kalibro.core.persistence.ProcessingDatabaseDao;
import org.kalibro.tests.UnitTest;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ProcessTask.class)
public class ProcessTaskTest extends UnitTest {

	private static final Long EXECUTION_TIME = new Random().nextLong();

	private Repository repository;
	private Processing processing;
	private ProcessContext context;
	private ProcessingDatabaseDao processingDao;

	private PreparingTask preparingTask;
	private LoadingTask loadingTask;
	private CollectingTask collectingTask;
	private BuildingTask buildingTask;
	private AggregatingTask aggregatingTask;
	private CalculatingTask calculatingTask;

	private ProcessTask processTask;

	@Before
	public void setUp() throws Exception {
		mockContext();
		processTask = new ProcessTask(repository);
		verifyNew(ProcessContext.class).withArguments(repository);
		mockSubtasks();
		processTask.perform();
	}

	private void mockContext() throws Exception {
		repository = mock(Repository.class);
		processing = mock(Processing.class);
		processingDao = mock(ProcessingDatabaseDao.class);
		context = new ProcessContext(repository);

		whenNew(ProcessContext.class).withArguments(repository).thenReturn(context);
		context.processingDao = processingDao;
		context.processing = processing;
		when(repository.getId()).thenReturn(new Random().nextLong());
		when(processing.getState()).thenReturn(ProcessState.LOADING);
	}

	private void mockSubtasks() throws Exception {
		preparingTask = mockSubtask(PreparingTask.class);
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
		InOrder order = Mockito.inOrder(preparingTask, loadingTask, collectingTask, buildingTask, aggregatingTask,
			calculatingTask);
		order.verify(preparingTask).addListener(processTask);
		order.verify(preparingTask).execute();
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
}