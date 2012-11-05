package org.kalibro.core.processing;

import java.util.Random;

import javax.mail.Message.RecipientType;

import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.*;
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

	private static final Long EXECUTION_TIME = new Random().nextLong();

	private Mailer mailer;
	private Processing processing;
	private ProcessingDatabaseDao processingDao;

	private ProcessTask processTask;

	@Before
	public void setUp() throws Exception {
		processing = mock(Processing.class);
		processingDao = mock(ProcessingDatabaseDao.class);
		Repository repository = mock(Repository.class);
		mockProcessingDao(repository);
		mockMailer();
		processTask = new ProcessTask(repository);
	}

	private void mockProcessingDao(Repository repository) throws Exception {
		DatabaseDaoFactory daoFactory = mock(DatabaseDaoFactory.class);
		whenNew(DatabaseDaoFactory.class).withNoArguments().thenReturn(daoFactory);
		when(daoFactory.createProcessingDao()).thenReturn(processingDao);
		when(processingDao.createProcessingFor(repository)).thenReturn(processing);
	}

	private void mockMailer() {
		KalibroSettings kalibroSettings = mock(KalibroSettings.class);
		MailSettings mailSettings = mock(MailSettings.class);
		mailer = mock(Mailer.class);
		mockStatic(KalibroSettings.class);
		when(KalibroSettings.load()).thenReturn(kalibroSettings);
		when(kalibroSettings.getMailSettings()).thenReturn(mailSettings);
		when(mailSettings.createMailer()).thenReturn(mailer);
	}

	@Test
	public void shouldPrepareAndExecuteSubtasks() throws Exception {
		LoadingTask loadingTask = mockSubtask(LoadingTask.class);
		CollectingTask collectingTask = mockSubtask(CollectingTask.class);
		AnalyzingTask analyzingTask = mockSubtask(AnalyzingTask.class);
		processTask.perform();
		InOrder order = Mockito.inOrder(loadingTask, collectingTask, analyzingTask);
		order.verify(loadingTask).prepare(processTask);
		order.verify(loadingTask).execute();
		order.verify(collectingTask).prepare(processTask);
		order.verify(collectingTask).executeInBackground();
		order.verify(analyzingTask).prepare(processTask);
		order.verify(analyzingTask).execute();
	}

	private <T extends ProcessSubtask> T mockSubtask(Class<T> subtaskClass) throws Exception {
		T subtask = mock(subtaskClass);
		whenNew(subtaskClass).withNoArguments().thenReturn(subtask);
		when(subtask.prepare(processTask)).thenReturn(subtask);
		return subtask;
	}

	@Test
	public void shouldUpdateProcessingOnTaskFinished() {
		when(processing.getState()).thenReturn(ProcessState.LOADING);
		processTask.taskFinished(report(null));

		InOrder order = Mockito.inOrder(processing, processingDao);
		order.verify(processing).setStateTime(ProcessState.READY, EXECUTION_TIME);
		order.verify(processing).setState(ProcessState.READY.nextState());
		order.verify(processingDao).save(processing);
	}

	@Test
	public void shouldUpdateProcessingOnTaskHalted() {
		Throwable error = mock(Throwable.class);
		when(processing.getState()).thenReturn(ProcessState.COLLECTING);
		processTask.taskFinished(report(error));

		InOrder order = Mockito.inOrder(processing, processingDao);
		order.verify(processing).setStateTime(ProcessState.READY, EXECUTION_TIME);
		order.verify(processing).setError(error);
		order.verify(processingDao).save(processing);
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

	@Test
	public void shouldSendMailAfterProcess() throws Exception {
		Email email = mock(Email.class);
		whenNew(Email.class).withNoArguments().thenReturn(email);
		processTask.perform();

		verify(mailer).sendMail(email);
		verify(email).addRecipient("aaa@example.com", "aaa@example.com", RecipientType.TO);
		verify(email).addRecipient("bbb@example.com", "bbb@example.com", RecipientType.TO);
	}
}