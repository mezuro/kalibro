package org.kalibro.core.processing;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.junit.Test;
import org.kalibro.*;
import org.kalibro.core.concurrent.Producer;
import org.kalibro.core.concurrent.Writer;
import org.kalibro.tests.UnitTest;

public class CollectingTaskTest extends UnitTest {

	private static final int BASE_TOOL_COUNT = 1 + new Random().nextInt(5);

	private File codeDirectory;
	private Writer<NativeModuleResult> resultWriter;
	private Producer<NativeModuleResult> resultProducer;
	private Map<BaseTool, Set<NativeMetric>> wantedMetrics;

	@Test
	public void shouldCollectMetricsFromBaseTools() throws Exception {
		codeDirectory = mock(File.class);
		resultWriter = mock(Writer.class);
		resultProducer = mock(Producer.class);
		mockWantedMetrics();
		new CollectingTask(mockContext()).perform();

		for (BaseTool baseTool : wantedMetrics.keySet())
			verify(baseTool).collectMetrics(codeDirectory, wantedMetrics.get(baseTool), resultWriter);
		verify(resultProducer, times(BASE_TOOL_COUNT)).createWriter();
	}

	private void mockWantedMetrics() {
		wantedMetrics = new HashMap<BaseTool, Set<NativeMetric>>();
		for (int i = 0; i < BASE_TOOL_COUNT; i++)
			wantedMetrics.put(mock(BaseTool.class), mock(Set.class));
	}

	private ProcessContext mockContext() {
		Repository repository = mock(Repository.class);
		Configuration configuration = mock(Configuration.class);
		ProcessContext processContext = mock(ProcessContext.class);
		when(processContext.codeDirectory()).thenReturn(codeDirectory);
		when(processContext.repository()).thenReturn(repository);
		when(repository.getConfiguration()).thenReturn(configuration);
		when(configuration.getNativeMetrics()).thenReturn(wantedMetrics);
		when(processContext.resultProducer()).thenReturn(resultProducer);
		when(resultProducer.createWriter()).thenReturn(resultWriter);
		return processContext;
	}
}