package org.kalibro.core.processing;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.junit.Test;
import org.kalibro.BaseTool;
import org.kalibro.Configuration;
import org.kalibro.NativeMetric;
import org.kalibro.NativeModuleResult;
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
		Configuration configuration = mock(Configuration.class);
		ProcessContext context = new ProcessContext(null);
		context.codeDirectory = codeDirectory;
		context.configuration = configuration;
		context.resultProducer = resultProducer;
		when(configuration.getNativeMetrics()).thenReturn(wantedMetrics);
		when(resultProducer.createWriter()).thenReturn(resultWriter);
		return context;
	}
}