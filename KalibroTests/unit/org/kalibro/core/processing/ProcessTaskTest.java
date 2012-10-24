package org.kalibro.core.processing;

import java.io.File;

import javax.mail.Message.RecipientType;

import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.*;
import org.kalibro.core.concurrent.Producer;
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

	private Mailer mailer;
	private Processing processing;

	private ProcessTask processTask;

	@Before
	public void setUp() throws Exception {
		processing = mock(Processing.class);
		Repository repository = mock(Repository.class);
		DatabaseDaoFactory daoFactory = mock(DatabaseDaoFactory.class);
		ProcessingDatabaseDao processingDao = mock(ProcessingDatabaseDao.class);
		whenNew(DatabaseDaoFactory.class).withNoArguments().thenReturn(daoFactory);
		when(daoFactory.createProcessingDao()).thenReturn(processingDao);
		when(processingDao.createProcessingFor(repository)).thenReturn(processing);
		processTask = new ProcessTask(repository);
		mockMailer();
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
	public void shouldExecuteChainedSubtasks() throws Exception {
		File codeDirectory = mockLoading();
		Producer<NativeModuleResult> resultProducer = mockProducer();
		CollectMetricsTask collectTask = mockCollecting(codeDirectory, resultProducer);
		AnalyzeResultsTask analysisTask = mockAnalysis(resultProducer);
		processTask.perform();
		InOrder order = Mockito.inOrder(collectTask, analysisTask);
		order.verify(collectTask).executeInBackground();
		order.verify(analysisTask).execute();
	}

	private File mockLoading() throws Exception {
		File codeDirectory = mock(File.class);
		LoadSourceTask loadTask = mock(LoadSourceTask.class);
		whenNew(LoadSourceTask.class).withArguments(processing).thenReturn(loadTask);
		when(loadTask.execute()).thenReturn(codeDirectory);
		return codeDirectory;
	}

	private Producer<NativeModuleResult> mockProducer() throws Exception {
		Producer<NativeModuleResult> resultProducer = mock(Producer.class);
		whenNew(Producer.class).withNoArguments().thenReturn(resultProducer);
		return resultProducer;
	}

	private CollectMetricsTask mockCollecting(File directory, Producer<NativeModuleResult> producer) throws Exception {
		CollectMetricsTask collectTask = mock(CollectMetricsTask.class);
		whenNew(CollectMetricsTask.class).withArguments(processing, directory, producer).thenReturn(collectTask);
		return collectTask;
	}

	private AnalyzeResultsTask mockAnalysis(Producer<NativeModuleResult> producer) throws Exception {
		AnalyzeResultsTask analysisTask = mock(AnalyzeResultsTask.class);
		whenNew(AnalyzeResultsTask.class).withArguments(processing, producer).thenReturn(analysisTask);
		return analysisTask;
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