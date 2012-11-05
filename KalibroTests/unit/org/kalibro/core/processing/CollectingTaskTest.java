package org.kalibro.core.processing;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.*;
import org.kalibro.core.concurrent.Producer;
import org.kalibro.core.concurrent.Writer;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest(CollectingTask.class)
public class CollectingTaskTest extends UnitTest {

	private File codeDirectory;
	private Configuration configuration;
	private Writer<NativeModuleResult> resultWriter;

	private CollectingTask collectingTask;

	@Before
	public void setUp() {
		collectingTask = spy(new CollectingTask());
		mockCodeDirectory();
		mockConfiguration();
		mockResultWriter();
	}

	private void mockCodeDirectory() {
		codeDirectory = mock(File.class);
		doReturn(codeDirectory).when(collectingTask).codeDirectory();
	}

	private void mockConfiguration() {
		configuration = mock(Configuration.class);
		Repository repository = mock(Repository.class);
		doReturn(repository).when(collectingTask).repository();
		when(repository.getConfiguration()).thenReturn(configuration);
	}

	private void mockResultWriter() {
		resultWriter = mock(Writer.class);
		Producer<NativeModuleResult> resultProducer = mock(Producer.class);
		doReturn(resultProducer).when(collectingTask).resultProducer();
		when(resultProducer.createWriter()).thenReturn(resultWriter);
	}

	@Test
	public void shouldCollectMetricsFromBaseTools() throws Exception {
		BaseTool baseTool = mock(BaseTool.class);
		Set<NativeMetric> wantedMetrics = mock(Set.class);
		Map<BaseTool, Set<NativeMetric>> wantedMetricsMap = new HashMap<BaseTool, Set<NativeMetric>>();
		wantedMetricsMap.put(baseTool, wantedMetrics);
		when(configuration.getNativeMetrics()).thenReturn(wantedMetricsMap);

		collectingTask.perform();
		verify(baseTool).collectMetrics(codeDirectory, wantedMetrics, resultWriter);
	}
}