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
import org.kalibro.core.persistence.DatabaseDaoFactory;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({CollectingTask.class, ProcessSubtask.class})
public class CollectingTaskTest extends UnitTest {

	private File codeDirectory;
	private Configuration configuration;
	private Writer<NativeModuleResult> resultWriter;

	private CollectingTask collectTask;

	@Before
	public void setUp() throws Exception {
		whenNew(DatabaseDaoFactory.class).withNoArguments().thenReturn(mock(DatabaseDaoFactory.class));
		codeDirectory = mock(File.class);
		collectTask = new CollectingTask(mockProcessing(), codeDirectory, mockProducer());
	}

	private Processing mockProcessing() {
		configuration = mock(Configuration.class);
		Repository repository = mock(Repository.class);
		Processing processing = mock(Processing.class);
		when(processing.getRepository()).thenReturn(repository);
		when(repository.getConfiguration()).thenReturn(configuration);
		return processing;
	}

	private Producer<NativeModuleResult> mockProducer() {
		resultWriter = mock(Writer.class);
		Producer<NativeModuleResult> producer = mock(Producer.class);
		when(producer.createWriter()).thenReturn(resultWriter);
		return producer;
	}

	@Test
	public void shouldCollectMetricsFromBaseTools() throws Exception {
		BaseTool baseTool = mock(BaseTool.class);
		Set<NativeMetric> wantedMetrics = mock(Set.class);
		Map<BaseTool, Set<NativeMetric>> wantedMetricsMap = new HashMap<BaseTool, Set<NativeMetric>>();
		wantedMetricsMap.put(baseTool, wantedMetrics);
		when(configuration.getNativeMetrics()).thenReturn(wantedMetricsMap);

		collectTask.compute();
		verify(baseTool).collectMetrics(codeDirectory, wantedMetrics, resultWriter);
	}
}