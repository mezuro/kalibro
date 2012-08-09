package br.jabuti;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.LinkedHashSet;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.model.enums.Language;

public class JabutiOutputParserTest {
	
	private JabutiOutputParser outputParser;
	private HashSet<NativeMetric> metrics;
	
	@Before
	public void setUp() {
		
		metrics = new LinkedHashSet<NativeMetric>();
		metrics.add(new NativeMetric("All Nodes Exception Independent", Granularity.APPLICATION, Language.JAVA));
		metrics.add(new NativeMetric("All Nodes Exception Dependent",   Granularity.APPLICATION, Language.JAVA));
		metrics.add(new NativeMetric("All Edges Exception Independent", Granularity.APPLICATION, Language.JAVA));
		metrics.add(new NativeMetric("All Edges Exception Dependent",   Granularity.APPLICATION, Language.JAVA));
		metrics.add(new NativeMetric("All Uses Exception Independent",  Granularity.APPLICATION, Language.JAVA));
		metrics.add(new NativeMetric("All Uses Exception Dependent",    Granularity.APPLICATION, Language.JAVA));
		metrics.add(new NativeMetric("All Pot Exception Independent",   Granularity.APPLICATION, Language.JAVA));
		metrics.add(new NativeMetric("All Pot Exception Dependent",     Granularity.APPLICATION, Language.JAVA));
		
		outputParser = new JabutiOutputParser();
	}
	
	@Test
	public void checkSupportedMetrics() throws IOException {
		InputStream metricListOutput = getClass().getResourceAsStream("Jabuti-Output-MetricList.txt");
		assertArrayEquals(metrics.toArray(), outputParser.getSupportedMetrics(metricListOutput).toArray());
	}

}
