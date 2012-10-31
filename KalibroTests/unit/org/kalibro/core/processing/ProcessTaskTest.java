package org.kalibro.core.processing;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.NativeModuleResult;
import org.kalibro.Processing;
import org.kalibro.Repository;
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
	}

	@Test
	public void shouldExecuteChainedSubtasks() throws Exception {
		File codeDirectory = mockLoading();
		Producer<NativeModuleResult> resultProducer = mockProducer();
		CollectingTask collectTask = mockCollecting(codeDirectory, resultProducer);
		AnalyzingTask analysisTask = mockAnalysis(resultProducer);
		processTask.perform();
		InOrder order = Mockito.inOrder(collectTask, analysisTask);
		order.verify(collectTask).executeInBackground();
		order.verify(analysisTask).execute();
	}

	private File mockLoading() throws Exception {
		File codeDirectory = mock(File.class);
		LoadingTask loadTask = mock(LoadingTask.class);
		whenNew(LoadingTask.class).withArguments(processing).thenReturn(loadTask);
		when(loadTask.execute()).thenReturn(codeDirectory);
		return codeDirectory;
	}

	private Producer<NativeModuleResult> mockProducer() throws Exception {
		Producer<NativeModuleResult> resultProducer = mock(Producer.class);
		whenNew(Producer.class).withNoArguments().thenReturn(resultProducer);
		return resultProducer;
	}

	private CollectingTask mockCollecting(File directory, Producer<NativeModuleResult> producer) throws Exception {
		CollectingTask collectTask = mock(CollectingTask.class);
		whenNew(CollectingTask.class).withArguments(processing, directory, producer).thenReturn(collectTask);
		return collectTask;
	}

	private AnalyzingTask mockAnalysis(Producer<NativeModuleResult> producer) throws Exception {
		AnalyzingTask analysisTask = mock(AnalyzingTask.class);
		whenNew(AnalyzingTask.class).withArguments(processing, producer).thenReturn(analysisTask);
		return analysisTask;
	}
}