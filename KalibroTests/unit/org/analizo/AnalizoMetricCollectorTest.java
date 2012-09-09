package org.analizo;

import static org.junit.Assert.*;

import java.io.File;
import java.io.InputStream;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeModuleResult;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AnalizoMetricCollector.class)
public class AnalizoMetricCollectorTest extends TestCase {

	private CommandTask executor;
	private AnalizoOutputParser parser;

	private AnalizoMetricCollector analizo;

	@Before
	public void setUp() throws Exception {
		executor = mock(CommandTask.class);
		parser = mock(AnalizoOutputParser.class);
		mockOutput("analizo metrics --list");
		analizo = new AnalizoMetricCollector();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkBaseTool() {
		BaseTool baseTool = analizo.getBaseTool();
		assertEquals("Analizo", baseTool.getName());
		assertEquals(AnalizoMetricCollector.class, baseTool.getCollectorClass());
		assertTrue(baseTool.getSupportedMetrics().isEmpty());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCollectMetrics() throws Exception {
		File codeDirectory = new File("/");
		Set<NativeMetric> metrics = mock(Set.class);
		InputStream output = mockOutput("analizo metrics /");
		Set<NativeModuleResult> results = mock(Set.class);
		when(parser.parseResults(output, metrics)).thenReturn(results);
		assertSame(results, analizo.collectMetrics(codeDirectory, metrics));
	}

	private InputStream mockOutput(String command) throws Exception {
		InputStream output = mock(InputStream.class);
		whenNew(CommandTask.class).withArguments(command).thenReturn(executor);
		when(executor.executeAndGetOuput()).thenReturn(output);
		whenNew(AnalizoOutputParser.class).withArguments(output).thenReturn(parser);
		return output;
	}
}