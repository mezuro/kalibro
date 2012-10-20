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
		CollectMetricsTask collectTask = mockCollecting(codeDirectory, resultProducer);
		AnalyzeResultsTask analysisTask = mockAnalysis(resultProducer);
		processTask.perform();
		InOrder order = Mockito.inOrder(collectTask, analysisTask);
		order.verify(collectTask).execute();
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
}