package org.kalibro.core.processing;

import java.io.File;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.NativeModuleResult;
import org.kalibro.Processing;
import org.kalibro.Repository;
import org.kalibro.core.persistence.DatabaseDaoFactory;
import org.kalibro.core.persistence.ProcessingDatabaseDao;
import org.kalibro.tests.UnitTest;
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
		Set<NativeModuleResult> results = mockCollecting(codeDirectory);
		AnalyzeResultsTask analysisTask = mockAnalysis(results);
		processTask.perform();
		verify(analysisTask).execute();
	}

	private File mockLoading() throws Exception {
		File codeDirectory = mock(File.class);
		LoadSourceTask loadSourceTask = mock(LoadSourceTask.class);
		whenNew(LoadSourceTask.class).withArguments(processing).thenReturn(loadSourceTask);
		when(loadSourceTask.execute()).thenReturn(codeDirectory);
		return codeDirectory;
	}

	private Set<NativeModuleResult> mockCollecting(File codeDirectory) throws Exception {
		Set<NativeModuleResult> results = mock(Set.class);
		CollectMetricsTask collectMetricsTask = mock(CollectMetricsTask.class);
		whenNew(CollectMetricsTask.class).withArguments(processing, codeDirectory).thenReturn(collectMetricsTask);
		when(collectMetricsTask.execute()).thenReturn(results);
		return results;
	}

	private AnalyzeResultsTask mockAnalysis(Set<NativeModuleResult> results) throws Exception {
		AnalyzeResultsTask analysisTask = mock(AnalyzeResultsTask.class);
		whenNew(AnalyzeResultsTask.class).withArguments(processing, results).thenReturn(analysisTask);
		return analysisTask;
	}
}