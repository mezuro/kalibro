package br.jabuti;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.model.BaseTool;
import org.kalibro.core.model.NativeMetric;
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

		metrics = new HashSet<NativeMetric>();
		metrics.add(new NativeMetric("All Nodes Exception Independent", Granularity.APPLICATION, Language.JAVA));
		metrics.add(new NativeMetric("All Nodes Exception Dependent",   Granularity.APPLICATION, Language.JAVA));
		metrics.add(new NativeMetric("All Edges Exception Independent", Granularity.APPLICATION, Language.JAVA));
		metrics.add(new NativeMetric("All Edges Exception Dependent",   Granularity.APPLICATION, Language.JAVA));
		metrics.add(new NativeMetric("All Uses Exception Independent",  Granularity.APPLICATION, Language.JAVA));
		metrics.add(new NativeMetric("All Uses Exception Dependent",    Granularity.APPLICATION, Language.JAVA));
		metrics.add(new NativeMetric("All Pot Exception Independent",   Granularity.APPLICATION, Language.JAVA));
		metrics.add(new NativeMetric("All Pot Exception Dependent",     Granularity.APPLICATION, Language.JAVA));

		
		parser = PowerMockito.mock(JabutiOutputParser.class);
		PowerMockito.whenNew(JabutiOutputParser.class).withNoArguments().thenReturn(parser);
		PowerMockito.when(parser.getSupportedMetrics()).thenReturn(metrics);
		
		jabuti = new JabutiMetricCollector();
	
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

}