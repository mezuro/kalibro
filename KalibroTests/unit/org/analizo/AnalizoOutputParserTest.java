package org.analizo;

import static org.kalibro.BaseToolFixtures.analizo;
import static org.kalibro.MetricFixtures.analizoMetric;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.NativeMetric;
import org.kalibro.NativeModuleResult;
import org.kalibro.tests.UnitTest;

public class AnalizoOutputParserTest extends UnitTest {

	private AnalizoOutputParser parser;

	@Before
	public void setUp() throws IOException {
		InputStream metricListOutput = getStream("Analizo-Output-MetricList.txt");
		parser = new AnalizoOutputParser(metricListOutput);
	}

	@Test
	public void shouldParseMetricListOutputToSupportedMetrics() {
		assertDeepEquals(analizo().getSupportedMetrics(), parser.getSupportedMetrics());
	}

	@Test
	public void shouldParseResultsOutputToModuleResults() {
		InputStream resultsOutput = getStream("Analizo-Output-HelloWorld.txt");
		Set<NativeMetric> metrics = analizo().getSupportedMetrics();
		Set<NativeModuleResult> results = parser.parseResults(resultsOutput, metrics);
		NativeModuleResult classResult = results.iterator().next();
		assertDoubleEquals(1.0, classResult.getResultFor(analizoMetric("accm")).getValue());
		assertDoubleEquals(4.0, classResult.getResultFor(analizoMetric("amloc")).getValue());
		assertDoubleEquals(2.0, classResult.getResultFor(analizoMetric("anpm")).getValue());
	}

	private InputStream getStream(String resourceName) {
		return getClass().getResourceAsStream(resourceName);
	}
}