package org.analizo;

import static org.kalibro.core.model.BaseToolFixtures.*;
import static org.kalibro.core.model.MetricFixtures.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeModuleResult;

public class AnalizoOutputParserTest extends TestCase {

	private AnalizoOutputParser parser;

	@Before
	public void setUp() throws IOException {
		InputStream metricListOutput = getResource("Analizo-Output-MetricList.txt");
		parser = new AnalizoOutputParser(metricListOutput);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldParseMetricListOutputToSupportedMetrics() {
		assertDeepEquals(analizo().getSupportedMetrics(), parser.getSupportedMetrics());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldParseResultsOutputToModuleResults() {
		InputStream resultsOutput = getResource("Analizo-Output-HelloWorld.txt");
		Set<NativeMetric> metrics = analizo().getSupportedMetrics();
		Set<NativeModuleResult> results = parser.parseResults(resultsOutput, metrics);
		NativeModuleResult classResult = results.iterator().next();
		assertDoubleEquals(1.0, classResult.getResultFor(analizoMetric("accm")).getValue());
		assertDoubleEquals(4.0, classResult.getResultFor(analizoMetric("amloc")).getValue());
		assertDoubleEquals(2.0, classResult.getResultFor(analizoMetric("anpm")).getValue());
	}

	private InputStream getResource(String resourceName) {
		return getClass().getResourceAsStream(resourceName);
	}
}