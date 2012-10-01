package org.analizo;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.NativeMetric;
import org.kalibro.NativeModuleResult;
import org.kalibro.core.command.CommandTask;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AnalizoMetricCollector.class)
public class AnalizoMetricCollectorTest extends UnitTest {

	private Map<NativeMetric, String> supportedMetrics;
	private AnalizoMetricCollector collector;

	@Before
	public void setUp() throws Exception {
		mockSupportedMetrics();
		collector = new AnalizoMetricCollector();
	}

	private void mockSupportedMetrics() throws Exception {
		supportedMetrics = mock(Map.class);
		CommandTask metricListTask = mock(CommandTask.class);
		InputStream metricListOutput = mock(InputStream.class);
		AnalizoMetricListParser metricListParser = mock(AnalizoMetricListParser.class);
		whenNew(CommandTask.class).withArguments("analizo metrics --list").thenReturn(metricListTask);
		when(metricListTask.executeAndGetOuput()).thenReturn(metricListOutput);
		whenNew(AnalizoMetricListParser.class).withArguments(metricListOutput).thenReturn(metricListParser);
		when(metricListParser.getSupportedMetrics()).thenReturn(supportedMetrics);
	}

	@Test
	public void shouldHaveNameAndDescription() throws IOException {
		assertEquals("Analizo", collector.name());
		assertEquals(loadResource("description"), collector.description());
	}

	@Test
	public void shouldGetSupportedMetrics() {
		Set<NativeMetric> metrics = mock(Set.class);
		when(supportedMetrics.keySet()).thenReturn(metrics);
		assertSame(metrics, collector.supportedMetrics());
	}

	@Test
	public void shouldCollectMetrics() throws Exception {
		File codeDirectory = mock(File.class);
		Set<NativeMetric> wantedMetrics = mock(Set.class);
		Set<NativeModuleResult> results = mock(Set.class);

		CommandTask analizoTask = mock(CommandTask.class);
		InputStream analizoOutput = mock(InputStream.class);
		AnalizoResultParser resultParser = mock(AnalizoResultParser.class);

		when(codeDirectory.getAbsolutePath()).thenReturn("~");
		whenNew(CommandTask.class).withArguments("analizo metrics ~").thenReturn(analizoTask);
		when(analizoTask.executeAndGetOuput()).thenReturn(analizoOutput);
		whenNew(AnalizoResultParser.class).withArguments(supportedMetrics, wantedMetrics).thenReturn(resultParser);
		when(resultParser.parse(analizoOutput)).thenReturn(results);

		assertSame(results, collector.collectMetrics(codeDirectory, wantedMetrics));
	}
}