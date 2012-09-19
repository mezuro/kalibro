package br.jabuti;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroException;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeModuleResult;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.model.enums.Language;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(JabutiMetricCollector.class)
public class JabutiMetricCollectorTest {
	
	private JabutiMetricCollector jabuti;
	private JabutiOutputParser parser;
	private HashSet<NativeMetric> metrics;
	
	@Before
	public void setUp() throws Exception {
		
		InputStream mockFile = getClass().getResourceAsStream("Mock.file");

		metrics = new HashSet<NativeMetric>();
		metrics.add(new NativeMetric("All Nodes Exception Independent", Granularity.SOFTWARE, Language.JAVA));
		metrics.add(new NativeMetric("All Nodes Exception Dependent",   Granularity.SOFTWARE, Language.JAVA));
		metrics.add(new NativeMetric("All Edges Exception Independent", Granularity.SOFTWARE, Language.JAVA));
		metrics.add(new NativeMetric("All Edges Exception Dependent",   Granularity.SOFTWARE, Language.JAVA));
		metrics.add(new NativeMetric("All Uses Exception Independent",  Granularity.SOFTWARE, Language.JAVA));
		metrics.add(new NativeMetric("All Uses Exception Dependent",    Granularity.SOFTWARE, Language.JAVA));
		metrics.add(new NativeMetric("All Pot Exception Independent",   Granularity.SOFTWARE, Language.JAVA));
		metrics.add(new NativeMetric("All Pot Exception Dependent",     Granularity.SOFTWARE, Language.JAVA));

		
		parser = PowerMockito.mock(JabutiOutputParser.class);
		PowerMockito.whenNew(JabutiOutputParser.class).withArguments(mockFile).thenReturn(parser);
		PowerMockito.when(parser.getSupportedMetrics()).thenReturn(metrics);
		
		CommandTask commandTaskMock = PowerMockito.mock(CommandTask.class);
		PowerMockito.whenNew(CommandTask.class).withArguments("jabuti --list").thenReturn(commandTaskMock);
		PowerMockito.when(commandTaskMock.executeAndGetOuput()).thenReturn(mockFile);
		
		jabuti = new JabutiMetricCollector();	
	}
	
	@Test(expected=KalibroException.class)
	public void constructorFailsIfJabutiIsNotPresent() throws Exception {
		PowerMockito.whenNew(CommandTask.class).withArguments("jabuti --list").thenThrow(new IOException(""));
		new JabutiMetricCollector();
	}

	@Test
	public void checkBaseTool() {
		BaseTool baseTool = jabuti.getBaseTool();
		assertNotNull(baseTool);
		assertEquals(JabutiMetricCollector.class, baseTool.getCollectorClass());
	}

	@Test
	public void getSupportedMetrics() {
		BaseTool baseTool = jabuti.getBaseTool();
		assertEquals(metrics.size(), baseTool.getSupportedMetrics().size());
		assertEquals(metrics, baseTool.getSupportedMetrics());
	}
	
	@Test
	public void checkCollectedMetrics() throws Exception{
		File project = new File(getClass().getResource("jabuti.conf").getFile()).getParentFile();
		
		CommandTask commandTaskMock = PowerMockito.mock(CommandTask.class);
		InputStream jabutiOutputMock = getClass().getResourceAsStream("Jabuti-Output-Vending.txt");
		HashSet<NativeModuleResult> result = new HashSet<NativeModuleResult>();
		
		PowerMockito.whenNew(CommandTask.class).withArguments("jabuti", project).thenReturn(commandTaskMock);
		PowerMockito.when(commandTaskMock.executeAndGetOuput()).thenReturn(jabutiOutputMock);
		PowerMockito.when(parser.parseResults(jabutiOutputMock, metrics)).thenReturn(result);
		
		assertEquals(result, jabuti.collectMetrics(project, metrics));
	}
	
	@Test(expected=KalibroException.class)
	public void checkFindWorkDirectory() throws Exception{
		File project = new File("/tmp");
		CommandTask commandTaskMock = PowerMockito.mock(CommandTask.class);
		PowerMockito.whenNew(CommandTask.class).withArguments("jabuti", project).thenReturn(commandTaskMock);
		jabuti.collectMetrics(project, metrics);
		
	}

}