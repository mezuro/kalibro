package org.cvsanaly;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.startsWith;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.NativeMetric;
import org.kalibro.NativeModuleResult;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.concurrent.Writer;
import org.kalibro.tests.UnitTest;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CvsAnalyMetricCollector.class)
public class CVSAnalyMetricCollectorTest extends UnitTest {

	private CvsAnalyMetricCollector cvsanaly;
	private CommandTask executor;
	private CvsAnalyDatabaseFetcher fetcher;

	private Writer<NativeModuleResult> resultWriter;

	@Before
	public void setUp() {
		cvsanaly = new CvsAnalyMetricCollector();
		executor = PowerMockito.mock(CommandTask.class);
		fetcher = PowerMockito.mock(CvsAnalyDatabaseFetcher.class);
		resultWriter = mock(Writer.class);
	}

	@Test
	public void checkBaseTool() {
		assertDeepEquals(CvsAnalyStub.getBaseTool().getSupportedMetrics(), cvsanaly.supportedMetrics());
	}

	@Test
	public void shouldDeleteFileIfAnExceptionOccurs() throws Exception {
		File codeDirectory = new File("/");
		File databaseFileSpy = mockDatabaseFile();
		Set<NativeMetric> metrics = cvsanaly.supportedMetrics();

		mockCommandToThrowException();
		mockFetcher();

		try {
			cvsanaly.collectMetrics(codeDirectory, metrics, resultWriter);
		} catch (Exception e) {
			assertNotNull(e);
		}
		Mockito.verify(databaseFileSpy).delete();
	}

	@Test
	public void shouldCollectAllMetrics() throws Exception {
		File codeDirectory = new File("/");
		Set<NativeMetric> metrics = cvsanaly.supportedMetrics();

		mockCommand();
		mockFetcher();

		cvsanaly.collectMetrics(codeDirectory, metrics, resultWriter);
		Mockito.verify(executor).execute();
		verifyResults(CvsAnalyStub.results());
	}

	@Test
	public void shouldCollectWantedMetrics() throws Exception {
		File codeDirectory = new File("/");
		Set<NativeMetric> metrics = new HashSet<NativeMetric>();
		metrics.add(CvsAnalyMetric.NUMBER_OF_LINES_OF_CODE.getNativeMetric());
		metrics.add(CvsAnalyMetric.MAXIMUM_CYCLOMATIC_COMPLEXITY.getNativeMetric());

		mockCommand();
		mockFetcher();

		cvsanaly.collectMetrics(codeDirectory, metrics, resultWriter);
		Mockito.verify(executor).execute();
		verifyResults(CvsAnalyStub.limitedResults());
	}

	private void mockFetcher() throws Exception {
		PowerMockito.whenNew(CvsAnalyDatabaseFetcher.class).withArguments(any()).thenReturn(fetcher);
		Mockito.when(fetcher.getMetricResults()).thenReturn(CvsAnalyStub.getExampleEntities());
	}

	private void mockCommand() throws Exception {
		String executionCommand = "cvsanaly2 -q --extensions=Metrics --db-driver=sqlite -d";
		PowerMockito.whenNew(CommandTask.class).withArguments(startsWith(executionCommand), any()).thenReturn(executor);
	}

	private void mockCommandToThrowException() throws Exception {
		String executionCommand = "cvsanaly2 -q --extensions=Metrics --db-driver=sqlite -d";
		PowerMockito.whenNew(CommandTask.class).withArguments(startsWith(executionCommand), any())
			.thenThrow(new Exception());
	}

	private File mockDatabaseFile() throws IOException {
		PowerMockito.mockStatic(File.class);
		File databaseFileSpy = spy(new File("/tmp/aaa"));
		PowerMockito.when(File.createTempFile("kalibro-cvsanaly-db", ".sqlite")).thenReturn(databaseFileSpy);
		return databaseFileSpy;
	}

	private void verifyResults(Set<NativeModuleResult> expected) {
		for (NativeModuleResult moduleResult : expected)
			verify(resultWriter).write(deepEq(moduleResult));
		verify(resultWriter).close();
	}
}