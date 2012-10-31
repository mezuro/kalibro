package org.kalibro.core.processing;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.ProcessState;
import org.kalibro.Processing;
import org.kalibro.core.concurrent.TaskReport;
import org.kalibro.core.persistence.DatabaseDaoFactory;
import org.kalibro.core.persistence.ProcessingDatabaseDao;
import org.kalibro.tests.UnitTest;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ProcessSubtask.class)
public class ProcessSubtaskTest extends UnitTest {

	private static final Long EXECUTION_TIME = new Random().nextLong();
	private static final String RESULT = "ProcessSubtaskTest result";

	private static final ProcessState TASK_STATE = ProcessState.READY;

	private Processing processing;
	private ProcessingDatabaseDao processingDao;

	private ProcessSubtask<String> subtask;

	@Before
	public void setUp() throws Exception {
		processing = mock(Processing.class);
		processingDao = mock(ProcessingDatabaseDao.class);
		DatabaseDaoFactory daoFactory = mock(DatabaseDaoFactory.class);
		whenNew(DatabaseDaoFactory.class).withNoArguments().thenReturn(daoFactory);
		when(daoFactory.createProcessingDao()).thenReturn(processingDao);
		when(processing.getState()).thenReturn(ProcessState.LOADING);
		subtask = new ReadyTask(processing);
	}

	@Test
	public void shouldListenToItself() {
		assertDeepEquals(set(subtask), Whitebox.getInternalState(subtask, "listeners"));
	}

	@Test
	public void shouldUpdateProcessingAfterExecution() {
		subtask.taskFinished(report(null));
		InOrder order = Mockito.inOrder(processing, processingDao);
		order.verify(processing).setStateTime(TASK_STATE, EXECUTION_TIME);
		order.verify(processing).setState(TASK_STATE.nextState());
		order.verify(processingDao).save(processing);
	}

	@Test
	public void shouldUpdateProcessingOnError() {
		Throwable error = mock(Throwable.class);
		subtask.taskFinished(report(error));
		InOrder order = Mockito.inOrder(processing, processingDao);
		order.verify(processing).setStateTime(eq(TASK_STATE), anyLong());
		order.verify(processing).setError(error);
		order.verify(processingDao).save(processing);
	}

	private TaskReport<String> report(Throwable error) {
		TaskReport<String> report = mock(TaskReport.class);
		when(report.getExecutionTime()).thenReturn(EXECUTION_TIME);
		when(report.getResult()).thenReturn(RESULT);
		when(report.isTaskDone()).thenReturn(error == null);
		when(report.getError()).thenReturn(error);
		return report;
	}

	@Test
	public void toStringShouldBeStateMessage() {
		String stateMessage = "ProcessSubtaskTest state message";
		when(processing.getStateMessage()).thenReturn(stateMessage);
		assertEquals(stateMessage, "" + subtask);
	}

	private final class ReadyTask extends ProcessSubtask<String> {

		private ReadyTask(Processing processing) {
			super(processing);
		}

		@Override
		protected String compute() {
			return RESULT;
		}
	}
}