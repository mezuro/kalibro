package br.jabuti;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.*;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.concurrent.Writer;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(JabutiMetricCollector.class)
public class JabutiMetricCollectorTest extends UnitTest {

	private JabutiOutputParser parser;
	private HashSet<NativeMetric> metrics;
	private Writer<NativeModuleResult> resultWriter;

	private JabutiMetricCollector jabuti;

	@Before
	public void setUp() throws Exception {

		InputStream mockFile = getClass().getResourceAsStream("Mock.file");

		metrics = new HashSet<NativeMetric>();
		metrics.add(new NativeMetric("All Nodes Exception Independent", Granularity.SOFTWARE, Language.JAVA));
		metrics.add(new NativeMetric("All Nodes Exception Dependent", Granularity.SOFTWARE, Language.JAVA));
		metrics.add(new NativeMetric("All Edges Exception Independent", Granularity.SOFTWARE, Language.JAVA));
		metrics.add(new NativeMetric("All Edges Exception Dependent", Granularity.SOFTWARE, Language.JAVA));
		metrics.add(new NativeMetric("All Uses Exception Independent", Granularity.SOFTWARE, Language.JAVA));
		metrics.add(new NativeMetric("All Uses Exception Dependent", Granularity.SOFTWARE, Language.JAVA));
		metrics.add(new NativeMetric("All Pot Exception Independent", Granularity.SOFTWARE, Language.JAVA));
		metrics.add(new NativeMetric("All Pot Exception Dependent", Granularity.SOFTWARE, Language.JAVA));

		parser = mock(JabutiOutputParser.class);
		whenNew(JabutiOutputParser.class).withArguments(mockFile).thenReturn(parser);
		when(parser.getSupportedMetrics()).thenReturn(metrics);

		CommandTask commandTaskMock = mock(CommandTask.class);
		whenNew(CommandTask.class).withArguments("jabuti --list").thenReturn(commandTaskMock);
		when(commandTaskMock.executeAndGetOuput()).thenReturn(mockFile);

		jabuti = new JabutiMetricCollector();
		resultWriter = mock(Writer.class);
	}

	@Test(expected = KalibroException.class)
	public void constructorFailsIfJabutiIsNotPresent() throws Exception {
		whenNew(CommandTask.class).withArguments("jabuti --list").thenThrow(new IOException(""));
		new JabutiMetricCollector();
	}

	@Test
	public void checkNameAndDescritpion() {
		assertEquals("Jabuti", jabuti.name());
		assertEquals("", jabuti.description());
	}

	@Test
	public void checkSupportedMetrics() {
		assertDeepEquals(metrics, jabuti.supportedMetrics());
	}

	@Test
	public void checkCollectedMetrics() throws Exception {
		File project = new File(getClass().getResource("jabuti.conf").getFile()).getParentFile();

		CommandTask commandTask = mock(CommandTask.class);
		InputStream jabutiOutput = mock(InputStream.class);

		whenNew(CommandTask.class).withArguments("jabuti", project).thenReturn(commandTask);
		when(commandTask.executeAndGetOuput()).thenReturn(jabutiOutput);

		jabuti.collectMetrics(project, metrics, resultWriter);
		verify(parser).parseResults(jabutiOutput, metrics, resultWriter);
	}

	@Test(expected = KalibroException.class)
	public void checkFindWorkDirectory() throws Exception {
		File project = new File("/tmp");
		CommandTask commandTaskMock = mock(CommandTask.class);
		whenNew(CommandTask.class).withArguments("jabuti", project).thenReturn(commandTaskMock);
		jabuti.collectMetrics(project, metrics, resultWriter);
	}
}