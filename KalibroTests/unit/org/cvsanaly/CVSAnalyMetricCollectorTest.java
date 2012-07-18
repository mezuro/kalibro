package org.cvsanaly;

import static org.mockito.Matchers.*;

import java.io.File;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeModuleResult;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CVSAnalyMetricCollector.class)
public class CVSAnalyMetricCollectorTest extends KalibroTestCase {

	private CVSAnalyMetricCollector cvsanaly;
	private CommandTask executor;
	private CVSAnalyDatabaseFetcher fetcher;

	@Before
	public void setUp() {
		cvsanaly = new CVSAnalyMetricCollector();
		executor = PowerMockito.mock(CommandTask.class);
		fetcher = PowerMockito.mock(CVSAnalyDatabaseFetcher.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkBaseTool() {
		assertDeepEquals(CVSAnalyStub.getBaseTool(), cvsanaly.getBaseTool());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCollectAllMetrics() throws Exception {
		File codeDirectory = new File("/");
		Set<NativeMetric> metrics = cvsanaly.getBaseTool().getSupportedMetrics();

		mockCommand();
		mockFetcher();

		Set<NativeModuleResult> actual = cvsanaly.collectMetrics(codeDirectory, metrics);
		Mockito.verify(executor).executeAndWait();
		assertDeepEquals(CVSAnalyStub.results(), actual);
	}

	private void mockFetcher() throws Exception {
		PowerMockito.whenNew(CVSAnalyDatabaseFetcher.class).withArguments(any()).thenReturn(fetcher);
		Mockito.when(fetcher.getMetricResults()).thenReturn(CVSAnalyStub.getExampleEntities());
	}

	private void mockCommand() throws Exception {
		String executionCommand = "cvsanaly2 -q --extensions=Metrics --db-driver=sqlite -d";
		PowerMockito.whenNew(CommandTask.class).withArguments(startsWith(executionCommand), any()).thenReturn(executor);
	}
}
