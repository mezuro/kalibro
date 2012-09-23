package org.cvsanaly;

import static org.mockito.Matchers.startsWith;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeModuleResult;
import org.kalibro.tests.UnitTest;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CVSAnalyMetricCollector.class)
public class CVSAnalyMetricCollectorTest extends UnitTest {

	private CVSAnalyMetricCollector cvsanaly;
	private CommandTask executor;
	private CVSAnalyDatabaseFetcher fetcher;

	@Before
	public void setUp() {
		cvsanaly = new CVSAnalyMetricCollector();
		executor = PowerMockito.mock(CommandTask.class);
		fetcher = PowerMockito.mock(CVSAnalyDatabaseFetcher.class);
	}

	@Test
	public void checkBaseTool() {
		assertDeepEquals(CVSAnalyStub.getBaseTool(), cvsanaly.getBaseTool());
	}

	@Test
	public void shouldDeleteFileIfAnExceptionOccurs() throws Exception {
		File codeDirectory = new File("/");
		File databaseFileSpy = mockDatabaseFile();
		Set<NativeMetric> metrics = cvsanaly.getBaseTool().getSupportedMetrics();

		mockCommandToThrowException();
		mockFetcher();

		try {
			cvsanaly.collectMetrics(codeDirectory, metrics);
		} catch (Exception e) {
			assertDifferent(e, null);
		}
		Mockito.verify(databaseFileSpy).delete();
	}

	@Test
	public void shouldCollectAllMetrics() throws Exception {
		File codeDirectory = new File("/");
		Set<NativeMetric> metrics = cvsanaly.getBaseTool().getSupportedMetrics();

		mockCommand();
		mockFetcher();

		Set<NativeModuleResult> actual = cvsanaly.collectMetrics(codeDirectory, metrics);
		Mockito.verify(executor).execute();
		assertDeepEquals(CVSAnalyStub.results(), actual);
	}

	@Test
	public void shouldCollectWantedMetrics() throws Exception {
		File codeDirectory = new File("/");
		Set<NativeMetric> metrics = new HashSet<NativeMetric>();
		metrics.add(CVSAnalyMetric.NUMBER_OF_LINES_OF_CODE.getNativeMetric());
		metrics.add(CVSAnalyMetric.MAXIMUM_CYCLOMATIC_COMPLEXITY.getNativeMetric());

		mockCommand();
		mockFetcher();

		Set<NativeModuleResult> actual = cvsanaly.collectMetrics(codeDirectory, metrics);
		Mockito.verify(executor).execute();
		assertDeepEquals(CVSAnalyStub.limitedResults(), actual);
	}

	private void mockFetcher() throws Exception {
		PowerMockito.whenNew(CVSAnalyDatabaseFetcher.class).withArguments(any()).thenReturn(fetcher);
		Mockito.when(fetcher.getMetricResults()).thenReturn(CVSAnalyStub.getExampleEntities());
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
}
